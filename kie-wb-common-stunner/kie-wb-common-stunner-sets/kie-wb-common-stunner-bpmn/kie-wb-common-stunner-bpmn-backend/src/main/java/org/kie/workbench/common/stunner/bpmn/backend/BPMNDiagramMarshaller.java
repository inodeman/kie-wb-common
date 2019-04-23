/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.backend;

import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.kie.workbench.common.stunner.bpmn.BPMNDefinitionSet;
import org.kie.workbench.common.stunner.bpmn.backend.marshall.json.builder.GraphObjectBuilderFactory;
import org.kie.workbench.common.stunner.bpmn.backend.marshall.json.oryx.OryxManager;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagramImpl;
import org.kie.workbench.common.stunner.bpmn.workitem.WorkItemDefinition;
import org.kie.workbench.common.stunner.bpmn.workitem.service.WorkItemDefinitionLookupService;
import org.kie.workbench.common.stunner.core.api.DefinitionManager;
import org.kie.workbench.common.stunner.core.api.FactoryManager;
import org.kie.workbench.common.stunner.core.backend.service.XMLEncoderDiagramMetadataMarshaller;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.kie.workbench.common.stunner.core.graph.command.GraphCommandManager;
import org.kie.workbench.common.stunner.core.graph.command.impl.GraphCommandFactory;
import org.kie.workbench.common.stunner.core.graph.processing.index.GraphIndexBuilder;
import org.kie.workbench.common.stunner.core.registry.impl.DefinitionsCacheRegistry;
import org.kie.workbench.common.stunner.core.rule.RuleManager;

@Dependent
public class BPMNDiagramMarshaller extends BaseDiagramMarshaller<BPMNDiagramImpl> {

    private final WorkItemDefinitionLookupService workItemDefinitionService;

    @Inject
    public BPMNDiagramMarshaller(final XMLEncoderDiagramMetadataMarshaller diagramMetadataMarshaller,
                                 final GraphObjectBuilderFactory bpmnGraphBuilderFactory,
                                 final DefinitionManager definitionManager,
                                 final GraphIndexBuilder<?> indexBuilder,
                                 final OryxManager oryxManager,
                                 final FactoryManager factoryManager,
                                 final DefinitionsCacheRegistry definitionsCacheRegistry,
                                 final RuleManager rulesManager,
                                 final GraphCommandManager graphCommandManager,
                                 final GraphCommandFactory commandFactory,
                                 final WorkItemDefinitionLookupService workItemDefinitionService) {
        super(diagramMetadataMarshaller,
              bpmnGraphBuilderFactory,
              definitionManager,
              indexBuilder,
              oryxManager,
              factoryManager,
              definitionsCacheRegistry,
              rulesManager,
              graphCommandManager,
              commandFactory);
        this.workItemDefinitionService = workItemDefinitionService;
    }

    @Override
    protected String getPreProcessingData(final Metadata metadata) {
        return workItemDefinitionService
                .execute(metadata)
                .stream()
                .map(WorkItemDefinition::getName)
                .collect(Collectors.joining(","));
    }

    @Override
    public Class<?> getDiagramDefinitionSetClass() {
        return BPMNDefinitionSet.class;
    }

    @Override
    public Class<? extends BPMNDiagramImpl> getDiagramDefinitionClass() {
        return BPMNDiagramImpl.class;
    }
}
