package io.temporal.closer.versioning.workflows;

import io.temporal.common.interceptors.ActivityInboundCallsInterceptor;
import io.temporal.common.interceptors.WorkerInterceptor;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptor;
import io.temporal.spring.boot.autoconfigure.template.WorkersTemplate;
import org.springframework.beans.factory.annotation.Qualifier;

public class VersionWorkerInterceptorImpl implements WorkerInterceptor {
  public VersionWorkerInterceptorImpl(
      @Qualifier("temporalWorkersTemplate") WorkersTemplate workersTemplate) {
    this.workersTemplate = workersTemplate;
  }

  private final WorkersTemplate workersTemplate;

  @Override
  public WorkflowInboundCallsInterceptor interceptWorkflow(WorkflowInboundCallsInterceptor next) {
    return new VersionWorkflowInboundCallsInterceptor(next, workersTemplate);
  }

  @Override
  public ActivityInboundCallsInterceptor interceptActivity(ActivityInboundCallsInterceptor next) {
    return next;
  }
}
