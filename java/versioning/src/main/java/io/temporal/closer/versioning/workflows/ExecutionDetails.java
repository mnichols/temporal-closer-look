package io.temporal.closer.versioning.workflows;

public record ExecutionDetails(
    String initialWorkflowImplementationType, String currentWorkflowImplementationType) {}
