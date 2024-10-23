package io.temporal.closer.versioning.workflows;

import org.springframework.stereotype.Component;

@Component("workflows-versioning-activities")
public class VersioningActivitiesImpl implements VersioningActivities {
  @Override
  public String act1(String value) {
    return String.format("act1-%s", value);
  }

  @Override
  public String act2(String value) {
    return String.format("act2-%s", value);
  }

  @Override
  public ExecutionDetails getExecutionDetails(String workflowImplementationType) {
    return new ExecutionDetails(workflowImplementationType, workflowImplementationType);
  }
}
