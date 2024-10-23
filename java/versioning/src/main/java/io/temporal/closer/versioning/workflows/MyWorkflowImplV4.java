package io.temporal.closer.versioning.workflows;

import static io.temporal.closer.versioning.workflows.Constants.ONE_YEAR;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class MyWorkflowImplV4 implements MyWorkflow {
  private final VersioningActivities acts;
  private MyWorkflowParams params;

  public MyWorkflowImplV4() {
    acts =
        Workflow.newActivityStub(
            VersioningActivities.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(3)).build());
  }

  @Override
  public void execute(MyWorkflowParams params) {
    this.params = params;

    acts.act1(params.value());
    var ver = Workflow.getVersion("fix1", Workflow.DEFAULT_VERSION, 1);
    if (ver == 1) {
      // introduce activity with versioning
      acts.act2(params.value());
    }
    var ver2 = Workflow.getVersion("fix2", Workflow.DEFAULT_VERSION, 2);
    for (var i = 0; i < 10; i++) {
      acts.act1(String.format("[%d]%s", i, params.value()));
      // introduce with incorrect Versioning
      if (ver2 == 2) {
        acts.act2(String.format("[%d]%s", i, params.value()));
      }
    }
    Workflow.sleep(ONE_YEAR);
  }

  @Override
  public MyWorkflowParams getParams() {
    return this.params;
  }
}
