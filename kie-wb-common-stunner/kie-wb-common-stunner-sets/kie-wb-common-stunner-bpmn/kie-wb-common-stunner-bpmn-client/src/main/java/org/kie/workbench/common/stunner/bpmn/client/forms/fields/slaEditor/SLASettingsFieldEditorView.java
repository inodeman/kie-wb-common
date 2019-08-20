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

package org.kie.workbench.common.stunner.bpmn.client.forms.fields.slaEditor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.common.client.dom.Anchor;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Event;
import org.jboss.errai.common.client.dom.TextInput;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.timerEditor.TimerSettingsFieldEditorView;
import org.kie.workbench.common.stunner.core.client.i18n.ClientTranslationService;
import org.uberfire.client.views.pfly.widgets.JQueryProducer;
import org.uberfire.client.views.pfly.widgets.Popover;

@Templated
public class SLASettingsFieldEditorView
        implements IsElement,
                   SLASettingsFieldEditorPresenter.View {

    static final String EMPTY_VALUE = "";

    static final String TimeDuration_Placeholder = "SLASettingsFieldEditorView.TimeDuration_Placeholder";

    static final String DurationTimer_Help_Header = "SLASettingsFieldEditorView.DurationTimer_Help_Header";

    static final String DurationTimer_Help_Line_1 = "SLASettingsFieldEditorView.DurationTimer_Help_Line_1";

    static final String Expression_Help_Line = "SLASettingsFieldEditorView.Expression_Help_Line";

    static final String PLACEHOLDER_ATTR = "placeholder";

    static final String DATA_CONTENT_ATTR = "data-content";

    @Inject
    @DataField("sla-duration-timer-help")
    private Anchor durationTimerHelp;

    @Inject
    private JQueryProducer.JQuery<Popover> durationTimerHelpPopover;

    @Inject
    @DataField("sla-duration-timer-params")
    private Div durationTimerParamsContainer;

    @Inject
    @DataField("sla-time-duration")
    private TextInput timeDuration;
    @Inject
    private ClientTranslationService translationService;

    private SLASettingsFieldEditorPresenter presenter;

    @Override
    public void init(SLASettingsFieldEditorPresenter presenter) {
        this.presenter = presenter;
    }

    @PostConstruct
    public void init() {
        timeDuration.setAttribute(PLACEHOLDER_ATTR,
                                  translationService.getValue(TimeDuration_Placeholder));

        durationTimerHelp.setAttribute(DATA_CONTENT_ATTR,
                                       getDurationTimerHtmlHelpText());
        durationTimerHelpPopover.wrap(durationTimerHelp).popover();
    }

    @Override
    public void setTimeDuration(String timeDuration) {
        this.timeDuration.setValue(timeDuration);
    }

    @Override
    public String getTimeDuration() {
        return timeDuration.getValue();
    }

    @Override
    public void clear() {
        setTimeDuration(EMPTY_VALUE);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        timeDuration.setDisabled(readOnly);
    }

    private String getDurationTimerHtmlHelpText() {
        return TimerSettingsFieldEditorView.buildHtmlHelpText(translationService.getValue(DurationTimer_Help_Header),
                                                              translationService.getValue(DurationTimer_Help_Line_1),
                                                              translationService.getValue(Expression_Help_Line));
    }

    @EventHandler("sla-time-duration")
    void onTimeDurationChange(@ForEvent("change") final Event event) {
        presenter.onTimerDurationChange();
    }
}
