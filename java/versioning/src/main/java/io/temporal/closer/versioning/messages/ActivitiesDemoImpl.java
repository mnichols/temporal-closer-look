package io.temporal.closer.versioning.messages;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.apache.commons.lang3.NotImplementedException;

public class ActivitiesDemoImpl implements VersioningWorkflow {
  VersioningActivities acts =
      Workflow.newActivityStub(
          VersioningActivities.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(3)).build());
  private StartVersioning params;

  @Override
  public void execute(StartVersioning params) {
    this.params = params;
    var response = acts.call(new ActivityInputRequest(params.name()));
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
    // no op
  }
}
