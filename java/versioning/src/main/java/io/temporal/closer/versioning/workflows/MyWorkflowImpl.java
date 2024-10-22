package io.temporal.closer.versioning.workflows;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWorkflowImpl implements MyWorkflow {
  private final Logger logger = LoggerFactory.getLogger(MyWorkflowImpl.class);
  private final VersioningActivities acts;
  private String value;

  public MyWorkflowImpl() {
    this.acts =
        Workflow.newActivityStub(
            VersioningActivities.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(10)).build());
  }

  @Override
  public void execute(MyWorkflowParams params) {
    this.value = params.value();
    this.value = this.acts.act1(this.value);
    for (int i = 0; i < 4; i++) {
      this.value = this.acts.act2(this.value);
      int simplest1 = Workflow.getVersion("simplest", Workflow.DEFAULT_VERSION, 1);
      if (simplest1 == 1) {
        this.value = this.acts.act1(this.value);
      }
    }

    //    int simplest1 = Workflow.getVersion("simplest", Workflow.DEFAULT_VERSION, 1);
    //    if (simplest1 == Workflow.DEFAULT_VERSION) {
    //      String val = Workflow.sideEffect(String.class, () -> "first");
    //      logger.info("val : {}", val);
    //    } else {
    //      String val = Workflow.sideEffect(String.class, () -> "second");
    //      logger.info("val : {}", val);
    //    }
    //
    //    int simplest2 = Workflow.getVersion("simplest", Workflow.DEFAULT_VERSION, 2);
    //    logger.info("simplest2 : {}", simplest2);
  }

  @Override
  public String getValue() {
    return this.value;
  }
}
