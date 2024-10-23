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
  private final ExecutionDetailsProvider executionDetailsProvider;
  private MyWorkflowParams params;

  public MyWorkflowImplV2() {
    this.executionDetailsProvider = new ExecutionDetailsProviderImpl();
    acts =
        Workflow.newActivityStub(
            VersioningActivities.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(3)).build());
  }

  @Override
  public void execute(MyWorkflowParams params) {
    this.executionDetailsProvider.init(getClass().getTypeName());

    /* **********
    This is the business logic we will modify over time
    ************/
    this.params = params;
    acts.act1(params.value());
    for (var i = 0; i < 3; i++) {
      // this change to command input does not require a version
      acts.act1(String.format("[%d]%s", i, params.value()));
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
