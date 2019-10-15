/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.core.client.session.command.impl;

import java.util.HashMap;

import javax.enterprise.event.Event;
import javax.inject.Singleton;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.stunner.core.client.api.SessionManager;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.event.selection.CanvasClearSelectionEvent;
import org.kie.workbench.common.stunner.core.client.command.CanvasCommandFactory;
import org.kie.workbench.common.stunner.core.client.command.SessionCommandManager;
import org.kie.workbench.common.stunner.core.client.session.ClientSession;
import org.kie.workbench.common.stunner.core.client.session.impl.EditorSession;
import org.kie.workbench.common.stunner.core.util.DefinitionUtils;

@Singleton
public class SessionSingletonCommandsFactory {

    private static HashMap<ClientSession, CopySelectionSessionCommand> copySessionInstances = new HashMap<>();

    private static HashMap<ClientSession, DeleteSelectionSessionCommand> deleteSessionInstances = new HashMap<>();

    public static void createOrPut(AbstractSelectionAwareSessionCommand<EditorSession> aClass, SessionManager sessionManager) {

        if (aClass instanceof CopySelectionSessionCommand) {
            if (copySessionInstances.containsKey(sessionManager.getCurrentSession())) { // there is one already one
                throw new IllegalStateException("Only one instance per Client Session can exist");
            }
            copySessionInstances.put(sessionManager.getCurrentSession(), (CopySelectionSessionCommand) aClass);
        } else if (aClass instanceof DeleteSelectionSessionCommand) {
            if (deleteSessionInstances.containsKey(sessionManager.getCurrentSession())) { // there is one already one
                throw new IllegalStateException("Only one instance per Client Session can exist");
            }
            deleteSessionInstances.put(sessionManager.getCurrentSession(), (DeleteSelectionSessionCommand) aClass);
        }
    }

    public static AbstractSelectionAwareSessionCommand<EditorSession> getInstanceCopy(
            final Event<?> commandExecutedEvent,
            SessionManager sessionManager) {

        final ClientSession currentSession = sessionManager.getCurrentSession();

        if (!copySessionInstances.containsKey(currentSession)) {
            return new CopySelectionSessionCommand((Event<CopySelectionSessionCommandExecutedEvent>) commandExecutedEvent, sessionManager);
        }

        return copySessionInstances.get(currentSession);
    }

    public static AbstractSelectionAwareSessionCommand<EditorSession> getInstanceDelete(
            final SessionCommandManager<AbstractCanvasHandler> sessionCommandManager,
            final ManagedInstance<CanvasCommandFactory<AbstractCanvasHandler>> canvasCommandFactoryInstance,
            final Event<CanvasClearSelectionEvent> clearSelectionEvent,
            final DefinitionUtils definitionUtils,
            final SessionManager sessionManager) {
        final ClientSession currentSession = sessionManager.getCurrentSession();

        if (!deleteSessionInstances.containsKey(currentSession)) {
            return new DeleteSelectionSessionCommand(sessionCommandManager, canvasCommandFactoryInstance, clearSelectionEvent, definitionUtils, sessionManager);
        }

        return deleteSessionInstances.get(currentSession);
    }
}
