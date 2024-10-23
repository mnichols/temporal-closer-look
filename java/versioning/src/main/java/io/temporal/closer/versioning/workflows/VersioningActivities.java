package io.temporal.closer.versioning.workflows;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface VersioningActivities {
  @ActivityMethod
  String act1(String value);

  @ActivityMethod
  String act2(String value);

  @ActivityMethod
  ExecutionDetails getExecutionDetails(String workflowImplementationType);
}
