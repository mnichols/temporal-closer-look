package io.temporal.closer.versioning.messages;

import org.springframework.stereotype.Component;

@Component("versioning-activities")
public class ActivitiesImpl implements VersioningActivities {
  @Override
  public ActivityOutputResponse call(ActivityInputRequest request) {
    return new ActivityOutputResponse(request.name());
  }
}
