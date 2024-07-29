package io.temporal.closer.versioning.messages;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface VersioningWorkflow {
  @WorkflowMethod
  void execute(StartVersioning params);

  @QueryMethod
  StartVersioning getParams();

  @QueryMethod
  SignalInputRequest getSignalInput();

  @SignalMethod
  void execute(SignalInputRequest params);
}
