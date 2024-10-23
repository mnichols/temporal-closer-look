package io.temporal.closer.versioning.workflows;

import static io.temporal.closer.versioning.workflows.Constants.ONE_YEAR;

import io.temporal.activity.ActivityOptions;
import io.temporal.activity.LocalActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWorkflowImplV1 implements MyWorkflow {
  private final VersioningActivities la;
  private final ExecutionDetailsProvider executionDetailsProvider;
  private Logger logger = LoggerFactory.getLogger(MyWorkflowImplV1.class);
  private final VersioningActivities acts;
  private MyWorkflowParams params;

  public MyWorkflowImplV1() {
    this.executionDetailsProvider = new ExecutionDetailsProviderImpl();
    acts =
        Workflow.newActivityStub(
            VersioningActivities.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(3)).build());
    la =
        Workflow.newLocalActivityStub(
            VersioningActivities.class,
            LocalActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(3))
                .build());
  }

  @Override
  public void execute(MyWorkflowParams params) {
    // THIS MUST BE FIRST
    this.executionDetailsProvider.init(getClass().getTypeName());

    /* **********
    This is the business logic we will modify over time
    ************/
    this.params = params;
    logger.info("executing {}", params);
    acts.act1(params.value());
    for (var i = 0; i < 3; i++) {
      acts.act1(params.value());
    }
    Workflow.sleep(ONE_YEAR);
  }

  @Override
  public MyWorkflowParams getParams() {
    return this.params;
  }

  @Override
  public ExecutionDetails getExecutionDetails() {
    return this.executionDetailsProvider.getCurrentExecutionDetails(getClass().getTypeName());
  }
}
