// package io.temporal.closer.versioning.workflows;
//
// import io.temporal.workflow.Workflow;
// import java.util.Objects;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// public class MyWorkflowProxyImpl implements MyWorkflow {
//  private MyWorkflow impl;
//  private final Logger logger = LoggerFactory.getLogger(MyWorkflowProxyImpl.class);
//
//  //  @WorkflowInit
//  //  public MyWorkflowProxyImpl(MyWorkflowParams params) {
//  //
//  //  }
//
//////  private MyWorkflowParams setImpl(MyWorkflowParams params) {
//////    var rootVersion =
//////        WorkflowsUtils.getRootVersion(Workflow.getInfo().getWorkflowId(), params.version());
//////    if (Objects.isNull(rootVersion)) {
//////      rootVersion = "V1";
//////    }
//////
//////    String finalRootVersion = rootVersion;
//////    var v =
//////        Objects.requireNonNullElseGet(
//////            params.version(), () -> new Version(finalRootVersion, Workflow.DEFAULT_VERSION));
//////    params = new MyWorkflowParams(params.value(), new Version(rootVersion, v.max()));
//////
//////    switch (rootVersion.toUpperCase()) {
//////      case "V1":
//////        impl = new MyWorkflowImplV1();
//////        break;
//////      case "V2":
//////        impl = new MyWorkflowImplV2();
//////        break;
//////      case "V3":
//////        impl = new MyWorkflowImplV3();
//////        break;
//////    }
////
////    logger.info(
////        "execution {} using root version {}", Workflow.getInfo().getWorkflowId(), rootVersion);
////    return params;
////  }
//
//  @Override
//  public void execute(MyWorkflowParams params) {
//    params = setImpl(params);
//    this.impl.execute(params);
//  }
//
//  @Override
//  public MyWorkflowParams getParams() {
//    return this.impl.getParams();
//  }
//
//  @Override
//  public ExecutionDetails getExecutionDetails() {
//    return null;
//  }
// }
