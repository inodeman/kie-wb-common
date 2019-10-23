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

package org.kie.workbench.common.stunner.core.client.canvas.event.selection;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.kie.workbench.common.stunner.core.client.canvas.CanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.event.AbstractCanvasHandlerEvent;

public final class CanvasSelectionEvent
        extends AbstractCanvasHandlerEvent<CanvasHandler> {

    private final Collection<String> identifiers;

    private final boolean isDragging; // GPS FIX

    private final boolean scheduleFormRendering; // GPS FIX

    public static boolean improvePerformance = false;

    public CanvasSelectionEvent(final CanvasHandler canvasHandler,
                                final Collection<String> identifiers) {
        super(canvasHandler);
        this.identifiers = new LinkedList<>(identifiers);
        this.isDragging = false;
        scheduleFormRendering = improvePerformance;
    }

    public CanvasSelectionEvent(final CanvasHandler canvasHandler,
                                final String uuid) {
        super(canvasHandler);
        this.identifiers = new LinkedList<>(Arrays.asList(uuid));
        this.isDragging = false;
        scheduleFormRendering = improvePerformance;
    }

    public CanvasSelectionEvent(final CanvasHandler canvasHandler,
                                final String uuid, final boolean isDragging, final boolean scheduleFormRendering) {
        super(canvasHandler);
        this.identifiers = new LinkedList<>(Arrays.asList(uuid));
        this.isDragging = isDragging;
        this.scheduleFormRendering = scheduleFormRendering;
    }

    public CanvasSelectionEvent(final CanvasHandler canvasHandler,
                                final String uuid, final boolean isDragging) {
        super(canvasHandler);
        this.identifiers = new LinkedList<>(Arrays.asList(uuid));
        this.isDragging = isDragging;
        scheduleFormRendering = improvePerformance;
    }

    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    public boolean isDragging() {
        return isDragging && improvePerformance;
    }

    public boolean isScheduleFormRendering() {
        return scheduleFormRendering;
    }
}