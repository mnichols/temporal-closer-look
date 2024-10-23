package io.temporal.closer.versioning.workflows;

import static io.temporal.closer.versioning.workflows.Constants.ONE_YEAR;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWorkflowImplV3 implements MyWorkflow {
  private static Logger logger = LoggerFactory.getLogger(MyWorkflowImplV3.class);
  private final VersioningActivities acts;
  private final ExecutionDetailsProvider executionDetailsProvider;
  private MyWorkflowParams params;

  public MyWorkflowImplV3() {
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

    /* This is the WRONG way to introduce activity without Versioning.
      This will fail on Replay since the Execution history does not "know" this command
      see just below to uncomment and swap out this errant line of code with a Versioned block
      COMMENT THIS BLOCK AFTER SEEING THE NDE
    */
    acts.act2(params.value() + "-v2");

    //
    /* This is the correct way to add a call to the Workflow
       UNCOMMENT THIS BLOCK
    */
    //    var addAct2 = Workflow.getVersion("add-act2", Workflow.DEFAULT_VERSION, 1);
    //    if (addAct2 == 1) {
    //      acts.act2(params.value() + "-v2");
    //    }

    for (var i = 0; i < 3; i++) {
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
