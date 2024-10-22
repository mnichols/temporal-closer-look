package io.temporal.closer.versioning.workflows;

import io.temporal.workflow.Workflow;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWorkflowProxyImpl implements MyWorkflow {
  private MyWorkflow impl;
  private final Logger logger = LoggerFactory.getLogger(MyWorkflowProxyImpl.class);

  //  @WorkflowInit
  //  public MyWorkflowProxyImpl(MyWorkflowParams params) {
  //
  //  }

  private void setImpl(MyWorkflowParams params) {
    var rootVersion = Objects.requireNonNull(params.version().root(), () -> "V1");
    switch (rootVersion.toUpperCase()) {
      case "V1":
        impl = new MyWorkflowImplV1(params);
        break;
      case "V2":
        impl = new MyWorkflowImplV2(params);
        break;
      case "V3":
        impl = new MyWorkflowImplV3(params);
        break;
    }
    logger.info(
        "execution {} using root version {}", Workflow.getInfo().getWorkflowId(), rootVersion);
  }

  @Override
  public void execute(MyWorkflowParams params) {
    setImpl(params);
    this.impl.execute(params);
  }

  @Override
  public String getValue() {
    return this.impl.getValue();
  }
}
