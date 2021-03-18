/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.backend.converters.tostunner.processes;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.kie.workbench.common.stunner.bpmn.backend.converters.customproperties.DeclarationList;
import org.kie.workbench.common.stunner.bpmn.backend.converters.customproperties.ParsedAssignmentsInfo;
import org.kie.workbench.common.stunner.bpmn.backend.converters.tostunner.BpmnNode;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagram;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.AssignmentsInfo;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.AbstractDataTypeCache;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.BaseDiagramSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.BaseAdvancedData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.BaseProcessData;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.view.View;
import org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl;

@Dependent
public class DataTypeCacheServer extends AbstractDataTypeCache {

    public DataTypeCacheServer() {
    }

    protected void cacheDataTypes(Object processRoot) {
        final List<BpmnNode> children = ((BpmnNode) processRoot).getChildren();

        for (BpmnNode node : children) {
            ViewImpl view = (ViewImpl) node.value().getContent();
            extractFromItem(view);
        }
    }

    protected List<String> processAssignments(AssignmentsInfo info) {
        List<String> dataTypes = new ArrayList<>();
        ParsedAssignmentsInfo assignments = ParsedAssignmentsInfo.of(info);

        assignments.getInputs().getDeclarations().forEach(declaration -> {
            dataTypes.add(declaration.getType());
        });

        assignments.getOutputs().getDeclarations().forEach(declaration -> {
            dataTypes.add(declaration.getType());
        });

        return dataTypes;
    }

    protected List<String> getDataTypes(String variables) {
        List<String> dataTypeList = new ArrayList<>();
        DeclarationList list = DeclarationList.fromString(variables);
        list.getDeclarations().forEach(var -> dataTypeList.add(var.getType()));
        return dataTypeList;
    }

    public void initCache(BpmnNode diagramRoot) {
        this.initCache(diagramRoot, (Node<View<? extends BPMNDiagram<? extends BaseDiagramSet, ? extends BaseProcessData, ? extends BaseAdvancedData>>, Edge>) diagramRoot.value());
    }
}
