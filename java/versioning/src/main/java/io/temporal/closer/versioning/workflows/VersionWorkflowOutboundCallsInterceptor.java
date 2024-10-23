package io.temporal.closer.versioning.workflows;

import io.temporal.common.interceptors.WorkflowOutboundCallsInterceptor;
import io.temporal.common.interceptors.WorkflowOutboundCallsInterceptorBase;
import io.temporal.workflow.Workflow;

public class VersionWorkflowOutboundCallsInterceptor extends WorkflowOutboundCallsInterceptorBase {
  private ExecutionDetails executionDetails = null;

  public VersionWorkflowOutboundCallsInterceptor(WorkflowOutboundCallsInterceptor next) {
    super(next);
    Workflow.registerListener(
        (VersionInterceptorListener)
            () -> {
              return this.executionDetails;
            });
  }

  public void setExecutionDetails(ExecutionDetails executionDetails) {
    this.executionDetails = executionDetails;
  }
}
