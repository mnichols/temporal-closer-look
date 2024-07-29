package io.temporal.closer.versioning.messages;

import io.temporal.workflow.Workflow;

public class SignalsDemoImpl implements VersioningWorkflow {
  private StartVersioning params;
  private SignalInputRequest signalInput;

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
    return signalInput;
  }

  @Override
  public void execute(SignalInputRequest params) {
    this.signalInput = params;
  }
}
