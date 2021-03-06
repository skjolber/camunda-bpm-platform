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

package org.camunda.bpm.engine.impl.migration.validation.instance;

import org.camunda.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.camunda.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;

public class NoUnmappedLeafActivityInstanceValidator implements MigratingActivityInstanceValidator {

  public void validate(MigratingActivityInstance migratingInstance, MigratingProcessInstance migratingProcessInstance, MigratingActivityInstanceValidationReportImpl instanceReport) {
    ScopeImpl sourceScope = migratingInstance.getSourceScope();
    if (sourceScope.getActivities().isEmpty() && migratingInstance.getMigrationInstruction() == null) {
      instanceReport.addFailure("Leaf activity was not mapped by the migration");
    }
  }

}
