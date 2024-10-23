package io.temporal.closer.versioning.workflows;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MyWorkflow {
  @WorkflowMethod
  void execute(MyWorkflowParams params);

  @QueryMethod
  MyWorkflowParams getParams();
}
