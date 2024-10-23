package io.temporal.closer.versioning.workflows;

import io.temporal.activity.LocalActivityOptions;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptor;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptorBase;
import io.temporal.common.interceptors.WorkflowOutboundCallsInterceptor;
import io.temporal.spring.boot.autoconfigure.template.WorkersTemplate;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class VersionWorkflowInboundCallsInterceptor extends WorkflowInboundCallsInterceptorBase {
  private final WorkersTemplate template;
  private VersionWorkflowOutboundCallsInterceptor outInterceptor;

  public VersionWorkflowInboundCallsInterceptor(
      WorkflowInboundCallsInterceptor next, WorkersTemplate workersTemplate) {
    super(next);
    this.template = workersTemplate;
  }

  @Override
  public void init(WorkflowOutboundCallsInterceptor outboundCalls) {
    outInterceptor = new VersionWorkflowOutboundCallsInterceptor(outboundCalls);
    super.init(outboundCalls);
  }

  @Override
  public WorkflowOutput execute(WorkflowInput input) {
    var acts =
        Workflow.newLocalActivityStub(
            VersioningActivities.class,
            LocalActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(3))
                .build());
    //        outInterceptor.setExecutionDetails(acts.);
    //        Workflow.getInfo().getWorkflowType()
    return super.execute(input);
  }
}
