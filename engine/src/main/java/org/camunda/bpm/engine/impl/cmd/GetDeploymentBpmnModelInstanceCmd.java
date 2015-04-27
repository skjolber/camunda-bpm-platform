/*
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

/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.camunda.bpm.engine.impl.cmd;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.deploy.DeploymentCache;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

/**
 * Gives access to a deploy BPMN model instance which can be accessed by
 * the BPMN model API.
 *
 * @author Sebastian Menski
 */
public class GetDeploymentBpmnModelInstanceCmd implements Command<BpmnModelInstance>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String processDefinitionId;

  public GetDeploymentBpmnModelInstanceCmd(String processDefinitionId) {
    if (processDefinitionId == null || processDefinitionId.length() < 1) {
      throw new ProcessEngineException("The process definition id is mandatory, but '" + processDefinitionId + "' has been provided.");
    }
    this.processDefinitionId = processDefinitionId;
  }

  public BpmnModelInstance execute(CommandContext commandContext) {
    ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
    final DeploymentCache deploymentCache = configuration.getDeploymentCache();

    ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);

    AuthorizationManager authorizationManager = commandContext.getAuthorizationManager();
    authorizationManager.checkReadProcessDefinition(processDefinition);

    BpmnModelInstance modelInstance = commandContext.runWithoutAuthentication(new Callable<BpmnModelInstance>() {
      public BpmnModelInstance call() throws Exception {
        return deploymentCache.findBpmnModelInstanceForProcessDefinition(processDefinitionId);
      }
    });

    ensureNotNull("no BPMN model instance found for process definition id " + processDefinitionId, "modelInstance", modelInstance);
    return modelInstance;
  }
}
