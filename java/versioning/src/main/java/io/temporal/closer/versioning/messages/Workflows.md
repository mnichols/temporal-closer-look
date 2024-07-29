# Workflows

## Workflow Input Messages

This study shows what happens if parameter names change for workflow input messages.

### Demonstration

* Workflow Type: [VersioningWorkflow](VersioningWorkflow.java)
* Workflow Implementation: [WorkflowsDemoImpl](WorkflowsDemoImpl.java)

1. Run `Application`

2. Start workflow with:
```
temporal workflow start \
    --type VersioningWorkflow \
    --task-queue messages \
    --workflow-id vw1 \
    --input '{"id": "vw1", "name": "bob smith"}' 
```
3. Query workflow with `getParams`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getParams
```
4. _Observe_
```
Query result:
  QueryResult  {"id":"vw1","name":"bob smith"}
```

5. Stop `Application`
6. Rename `StartVersioning.java` property `name` to `fullName`
7. Run `Application`
8. Query workflow with `getParams`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getParams
```
9. _Observe_ an `ERROR` similar to:
```text
"querying workflow failed: io.temporal.internal.statemachines.InternalWorkflowTaskException: Failure handling event 4 of type 'EVENT_TYPE_WORKFLOW_TASK_COMPLETED' during replay
```
10. Rename `StartVersioning.java` property `fullName` to `name`
11. Run `Application`
12. Query workflow with `getParams`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getParams
```
13. _Observe_ restored success
```
Query result:
  QueryResult  {"id":"vw1","name":"bob smith"}
```

### Explanation

You will observe `Jackson` JSON errors in your ERROR message due to the inability for Temporal to replay the execution with
original input arguments which is caused by a Query (and many other cases).

> Replay of an execution happens at arbitrary times in a Temporal Worker. This breaking change will prohibit your executions from making progress.
