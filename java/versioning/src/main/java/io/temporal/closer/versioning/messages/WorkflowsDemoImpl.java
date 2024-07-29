package io.temporal.closer.versioning.messages;

import io.temporal.workflow.Workflow;
import org.apache.commons.lang3.NotImplementedException;

public class WorkflowsDemoImpl implements VersioningWorkflow {
  private StartVersioning params;

  @Override
  public void execute(StartVersioning params) {
    this.params = params;
    Workflow.await(() -> false);
  }

  @Override
  public StartVersioning getParams() {
    return params;
  }

  @Override
  public SignalInputRequest getSignalInput() {
    throw new NotImplementedException();
  }

  @Override
  public void execute(SignalInputRequest params) {
    throw new NotImplementedException();
  }
}
