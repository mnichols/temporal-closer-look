package io.temporal.closer.versioning.workflows;

public record MyWorkflowParams(String value, Version version) {
  MyWorkflowParams() {
    this("", null);
  }
}
