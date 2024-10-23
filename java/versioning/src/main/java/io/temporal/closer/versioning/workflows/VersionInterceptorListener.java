package io.temporal.closer.versioning.workflows;

import io.temporal.workflow.QueryMethod;

public interface VersionInterceptorListener {
  @QueryMethod
  ExecutionDetails getExecutionDetails();
}
