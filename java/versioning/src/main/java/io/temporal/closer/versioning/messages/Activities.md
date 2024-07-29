# Activities

## Activity Input Messages

This study shows what happens if parameter names change for Activity input messages.

### Demonstration : Inputs

* Workflow Type: [VersioningWorkflow](VersioningWorkflow.java)
* Workflow Implementation: [ActivitiesDemoImpl](ActivitiesDemoImpl.java)

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
6. Rename `ActivityInputRequest.java` property `name` to `fullName`
7. Run `Application`
8. Query workflow with `getParams`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getParams
```
9. _Observe_ NO `ERROR` and the same response as above
10. Rename `ActivityInputRequest.java` property `fullName` to `name`
11. Run `Application`
12. Query workflow with `getParams`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getParams
```
13. _Observe_ NO change
```
Query result:
  QueryResult  {"id":"vw1","name":"bob smith"}
```

#### Explanation

`input` is serialized as a part of the `ActivityTaskScheduled` event.

You can see this in action by running:
```text
temporal workflow show \
    --workflow-id vw1 \
    --output json | jq -c '[ .events | .[] | select(.eventType | contains("ACTIVITY_TASK_SCHEDULED")) ]'
```

> This means that changing input arguments _to Activities_ does not:
> 1. cause non-determinism errors (NDE).
> 2. raise errors during replay

### Demonstration : Results

* Workflow Type: [VersioningWorkflow](VersioningWorkflow.java)
* Workflow Implementation: [ActivitiesDemoImpl](ActivitiesDemoImpl.java)

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
6. Rename `ActivityOutputRequest.java` property `name` to `fullName`
7. Run `Application`
8. Query workflow with `getParams`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getParams
```
9. _Observe_ `ERROR` due to replay failure like
```text
Failure handling event 10 of type 'EVENT_TYPE_WORKFLOW_TASK_COMPLETED' during replay. {WorkflowTaskStartedEventId=13, CurrentStartedEventId=9}
```
10. Signal workflow with `execute` Signal:
11. _Observe_ a `WorkflowTaskFailed` appears in your history because of a Non-Determinism problem like
```text
Failure handling event 10 of type 'EVENT_TYPE_WORKFLOW_TASK_COMPLETED' during replay. {WorkflowTaskStartedEventId=13, CurrentStartedEventId=9}
```
12. Rename `ActivityOutputRequest.java` property `fullName` to `name`
13. Run `Application`
14. Query workflow with `getParams`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getParams
```
13. _Observe_ Errors have gone and Signals have been applied
```
Query result:
  QueryResult  {"id":"vw1","name":"bob smith"}
```
> Note that you will need to wait a moment to see the `WorkflowTaskCompleted` due to the previous failures backing off retry attempts exponentially.


#### Explanation

Failures to replay due to changes in the `result` values being deserialized by the Worker at runtime
will cause the Workflow Task being handled to `WorkflowTaskFailed`. 

#### Recommendations

Replay errors like this should be investigated for breaking changes in the output values from Activities and rolled back.
If you must change the message contract for an existing Activity output/result, you should use the `Version` (aka `Patch`) strategy to avoid 
Non-Determinism error. This would therefore require a new version of the Activity function you are calling.
See the `Temporal:Patching` module for more details.