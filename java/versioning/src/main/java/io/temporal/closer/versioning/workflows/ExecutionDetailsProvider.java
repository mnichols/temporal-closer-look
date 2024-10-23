package io.temporal.closer.versioning.workflows;

public interface ExecutionDetailsProvider {
  void init(String workflowImplType);

  ExecutionDetails getInitialExecutionDetails();

  ExecutionDetails getCurrentExecutionDetails(String workflowImplType);
}
