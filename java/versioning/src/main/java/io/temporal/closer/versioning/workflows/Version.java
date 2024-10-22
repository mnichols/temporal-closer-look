package io.temporal.closer.versioning.workflows;

import io.temporal.workflow.Workflow;

public record Version(String root, int max, int min) {
  public Version(String root, int max) {
    this(root, max, Workflow.DEFAULT_VERSION);
  }
}
