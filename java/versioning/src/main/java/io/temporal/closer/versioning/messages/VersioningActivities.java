package io.temporal.closer.versioning.messages;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface VersioningActivities {
  ActivityOutputResponse call(ActivityInputRequest request);
}
