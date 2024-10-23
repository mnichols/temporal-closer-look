package io.temporal.closer.versioning.workflows;

import io.temporal.activity.LocalActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class ExecutionDetailsProviderImpl implements ExecutionDetailsProvider {
  private final VersioningActivities la;
  private ExecutionDetails initialExecutionDetails;

  public ExecutionDetailsProviderImpl() {
    la =
        Workflow.newLocalActivityStub(
            VersioningActivities.class,
            LocalActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(2))
                .build());
  }

  @Override
  public void init(String workflowImplType) {
    this.initialExecutionDetails = la.getExecutionDetails(workflowImplType);
  }

  @Override
  public ExecutionDetails getInitialExecutionDetails() {
    return this.initialExecutionDetails;
  }

  @Override
  public ExecutionDetails getCurrentExecutionDetails(String workflowImplType) {
    return new ExecutionDetails(
        initialExecutionDetails.initialWorkflowImplementationType(), workflowImplType);
  }
}
