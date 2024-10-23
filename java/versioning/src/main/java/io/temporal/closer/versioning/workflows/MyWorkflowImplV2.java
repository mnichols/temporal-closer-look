package io.temporal.closer.versioning.workflows;

import static io.temporal.closer.versioning.workflows.Constants.ONE_YEAR;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWorkflowImplV2 implements MyWorkflow {
  private final Logger logger = LoggerFactory.getLogger(MyWorkflowImplV2.class);
  private final VersioningActivities acts;
  private MyWorkflowParams params;

  public MyWorkflowImplV2(MyWorkflowParams params) {
    acts =
        Workflow.newActivityStub(
            VersioningActivities.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(3)).build());
  }

  @Override
  public void execute(MyWorkflowParams params) {
    this.params = params;
    logger.info("executing {}", params);
    acts.act1(params.value());
    for (var i = 0; i < 10; i++) {
      // this change to command input does not require a version
      acts.act1(String.format("[%d]%s", i, params.value()));
    }
    Workflow.sleep(ONE_YEAR);
  }

  @Override
  public MyWorkflowParams getParams() {
    return this.params;
  }
}
