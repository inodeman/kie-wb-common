/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.workbench.common.stunner.client.widgets.toolbar.command;

import java.lang.annotation.Annotation;

import com.google.gwt.core.client.GWT;
import org.gwtbootstrap3.client.ui.constants.IconRotate;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.stunner.client.widgets.resources.i18n.StunnerWidgetsConstants;
import org.kie.workbench.common.stunner.client.widgets.toolbar.Toolbar;
import org.kie.workbench.common.stunner.client.widgets.toolbar.ToolbarCommand;
import org.kie.workbench.common.stunner.core.client.i18n.ClientTranslationService;
import org.kie.workbench.common.stunner.core.client.session.ClientSession;
import org.kie.workbench.common.stunner.core.client.session.command.ClientSessionCommand;
import org.kie.workbench.common.stunner.core.client.session.impl.InstanceUtils;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.util.DefinitionUtils;
import org.kie.workbench.common.stunner.core.util.UUID;
import org.uberfire.ext.widgets.common.client.common.popups.YesNoCancelPopup;
import org.uberfire.mvp.Command;

public abstract class AbstractToolbarCommand<S extends ClientSession, C extends ClientSessionCommand<S>>
        implements ToolbarCommand<S> {

    private final String uuid;
    private final DefinitionUtils definitionUtils;
    private final ManagedInstance<C> commands;
    protected final ClientTranslationService translationService;
    private Toolbar<S> toolbar;
    private C command;

    protected AbstractToolbarCommand(final DefinitionUtils definitionUtils,
                                     final ManagedInstance<C> commands,
                                     final ClientTranslationService translationService) {
        GWT.log("Abstract Tool Bar Command New 1:" + this.getClass());
        this.uuid = UUID.uuid();
        this.definitionUtils = definitionUtils;
        this.commands = commands;
        this.translationService = translationService;
        GWT.log("Abstract Tool Bar Command New:" + this.getClass());
    }

    protected abstract boolean requiresConfirm();

    public ToolbarCommand<S> initialize(final Toolbar<S> toolbar,
                                        final S session) {
        this.toolbar = toolbar;
        final Diagram diagram = session.getCanvasHandler().getDiagram();
        final String id = diagram.getMetadata().getDefinitionSetId();
        final Annotation qualifier = definitionUtils.getQualifier(id);
        command = InstanceUtils.lookup(commands,
                                       qualifier);
        command.listen(this::checkState);
        command.bind(session);
        checkState();
        GWT.log("Abstract Tool Bar Command Initialize:" + this.getClass());
        //GWT.log("Abstract Tool Bar Command Initialize session:" + session.getSessionUUID());

        return this;
    }

    @Override
    public void execute() {
        GWT.log("Abstract Tool Bar Command execute:" + this.getClass());
        if (requiresConfirm()) {
            this.executeWithConfirm(noOpCallback);
        } else {
            this.executeWithNoConfirm(noOpCallback);
        }
    }

    public <V> void execute(final ClientSessionCommand.Callback<V> callback) {
        GWT.log("Abstract Tool Bar Command execute with callback:" + this.getClass());
        if (requiresConfirm()) {
            this.executeWithConfirm(callback);
        } else {
            this.executeWithNoConfirm(callback);
        }
    }

    protected String getConfirmMessage() {
        return translationService.getValue(StunnerWidgetsConstants.AbstractToolbarCommand_ConfirmMessage);
    }

    private <V> void executeWithConfirm(final ClientSessionCommand.Callback<V> callback) {
        final Command yesCommand = () -> {
            this.executeWithNoConfirm(callback);
        };
        final Command noCommand = () -> {
        };
        final YesNoCancelPopup popup = YesNoCancelPopup.newYesNoCancelPopup(getConfirmMessage(),
                                                                            null,
                                                                            yesCommand,
                                                                            noCommand,
                                                                            noCommand);
        popup.show();
        GWT.log("Abstract Tool Bar Command execute with confirm:" + this.getClass());
    }

    protected void checkState() {
        GWT.log("Abstract Tool Bar Command checkstate:" + this.getClass());
        GWT.log("Abstract Tool Bar Command checkstate enabled:" + command.isEnabled());

        if (command.isEnabled()) {
            enable();
        } else {
            disable();
        }
    }

    public void refresh() {
        checkState();
    }

    protected void executeWithConfirm(final Command command) {
        final Command yesCommand = () -> {
            command.execute();
        };
        final Command noCommand = () -> {
        };
        final YesNoCancelPopup popup = YesNoCancelPopup.newYesNoCancelPopup(getConfirmMessage(),
                                                                            null,
                                                                            yesCommand,
                                                                            noCommand,
                                                                            noCommand);
        popup.show();
        GWT.log("Abstract Tool Bar Command execute with confirm 2:" + this.getClass());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractToolbarCommand)) {
            return false;
        }
        AbstractToolbarCommand that = (AbstractToolbarCommand) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid == null ? 0 : ~~uuid.hashCode();
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public IconRotate getIconRotate() {
        return IconRotate.NONE;
    }

    @Override
    public final void destroy() {
        doDestroy();
        command.destroy();
        commands.destroy(command);
        command = null;
        toolbar = null;
    }

    protected void doDestroy() {
    }

    protected void enable() {
        toolbar.enable(this);
    }

    protected void disable() {
        GWT.log("Toolbar: " + toolbar);
        toolbar.disable(this);
    }

    private <V> void executeWithNoConfirm(final ClientSessionCommand.Callback<V> callback) {
        GWT.log("Abstract Tool Bar Command execute with no confirm:" + this.getClass());
        this.command.execute(callback);
    }

    private final ClientSessionCommand.Callback<Object> noOpCallback = new ClientSessionCommand.Callback<Object>() {
        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(final Object error) {
        }
    };
}
