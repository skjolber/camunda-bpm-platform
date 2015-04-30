package org.camunda.bpm.qa.upgrade.scenarios.boundary;

import java.util.List;

import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.qa.upgrade.ScenarioUnderTest;
import org.camunda.bpm.qa.upgrade.UpgradeTestRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

@ScenarioUnderTest("NonInterruptingBoundaryEventScenario")
public class NonInterruptingBoundaryEventScenarioTest {

  @Rule
  public UpgradeTestRule rule = new UpgradeTestRule();

  @Test
  @ScenarioUnderTest("initTimer.1")
  public void testInitTimerCompletionCase1() {
    // given
    Task afterBoundaryTask = rule.taskQuery().taskDefinitionKey("afterBoundaryTask").singleResult();

    // when
    rule.messageCorrelation("ReceiveTaskMessage").correlate();
    rule.getTaskService().complete(afterBoundaryTask.getId());

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("initTimer.2")
  public void testInitTimerCompletionCase2() {
    // given
    Task afterBoundaryTask = rule.taskQuery().taskDefinitionKey("afterBoundaryTask").singleResult();

    // when
    rule.getTaskService().complete(afterBoundaryTask.getId());
    rule.messageCorrelation("ReceiveTaskMessage").correlate();

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("initTimer.3")
  public void testInitTimerActivityInstanceTree() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    ActivityInstance activityInstance = rule.getRuntimeService().getActivityInstance(instance.getId());

    // then
    // TODO: assert the tree
    Assert.assertNotNull(activityInstance);
  }

  @Test
  @ScenarioUnderTest("initTimer.4")
  public void testInitTimerDeletion() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    rule.getRuntimeService().deleteProcessInstance(instance.getId(), null);

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("initTimer.5")
  public void testInitTimerTriggerBoundary() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when the boundary event is triggered another 2 times
    for (int i = 0; i < 2; i++) {
      Job job = rule.getManagementService().createJobQuery()
        .processInstanceId(instance.getId()).singleResult();
      rule.getManagementService().executeJob(job.getId());
    }

    // and the tasks are completed
    List<Task> afterBoundaryTasks = rule.taskQuery().list();
    Assert.assertEquals(3, afterBoundaryTasks.size());

    for (Task afterBoundaryTask : afterBoundaryTasks) {
      rule.getTaskService().complete(afterBoundaryTask.getId());
    }

    rule.messageCorrelation("ReceiveTaskMessage").correlate();

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("initMessage.1")
  public void testInitMessageCompletionCase1() {
    // given
    Task afterBoundaryTask = rule.taskQuery().taskDefinitionKey("afterBoundaryTask").singleResult();

    // when
    rule.messageCorrelation("ReceiveTaskMessage").correlate();
    rule.getTaskService().complete(afterBoundaryTask.getId());

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("initMessage.2")
  public void testInitMessageCompletionCase2() {
    // given
    Task afterBoundaryTask = rule.taskQuery().taskDefinitionKey("afterBoundaryTask").singleResult();

    // when
    rule.getTaskService().complete(afterBoundaryTask.getId());
    rule.messageCorrelation("ReceiveTaskMessage").correlate();

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("initMessage.3")
  public void testInitMessageActivityInstanceTree() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    ActivityInstance activityInstance = rule.getRuntimeService().getActivityInstance(instance.getId());

    // then
    // TODO: assert the tree
    Assert.assertNotNull(activityInstance);
  }

  @Test
  @ScenarioUnderTest("initMessage.4")
  public void testInitMessageDeletion() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    rule.getRuntimeService().deleteProcessInstance(instance.getId(), null);

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("initMessage.5")
  public void testInitMessageTriggerBoundary() {
    // when the boundary event is triggered another 2 times
    for (int i = 0; i < 2; i++) {
      rule.messageCorrelation("BoundaryEventMessage").correlate();
    }

    // and the tasks are completed
    List<Task> afterBoundaryTasks = rule.taskQuery().list();
    Assert.assertEquals(3, afterBoundaryTasks.size());

    for (Task afterBoundaryTask : afterBoundaryTasks) {
      rule.getTaskService().complete(afterBoundaryTask.getId());
    }

    rule.messageCorrelation("ReceiveTaskMessage").correlate();

    // then
    rule.assertScenarioEnded();
  }

}