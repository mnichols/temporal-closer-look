# Versioning: Workflows

This demonstrates an evolution of a Workflow definition called `MyWorkflow` that uses the _Patched_
Versioning strategy to make changes over time.

## How to simulate progressive changes

The `MyWorkflowProxyImpl` delegates to the implementation version you specify.
If you provide a `version.root` to input params it will use that version; eg "V2".
If you append `__v<version>` to the WorkflowId it will use that version.
If _both_ are provided, the `version.root` parameter will be the definition version used.

###### V1: Execute `MyWorkflow`

_Designate the implementation in the `workflow-classes` to `MyWorkflowImplV1`_

```yaml
  # resources/application.yaml
  temporal:
    test-server:
      enabled: false
    workers:
      # <snip>
      - task-queue: workflows
        workflow-classes:
          # this implementation name is what we will be redeploying to simulate change over time
          - io.temporal.closer.versioning.workflows.MyWorkflowImplV1
        activity-beans:
          - workflows-versioning-activities
```
Use the following command to start a workflow execution.
You will keep Executions we start "Open" as we make changes to the Workflow implementation to verify Replay.
Therefore, do not stop your Temporal service or delete the Executions.

```sh
temporal workflow start \
  --task-queue workflows \
  --type MyWorkflow \
  --workflow-id a1__v1 \
  --input '{"value":"foo"}'
```

Visit the Workflow Execution and observe the `open` execution.
You can view the version of the `MyWorkflow` being used with the `getParams` query.

```sh
temporal workflow query \
  --name getExecutionDetails \
  --workflow-id a1__v1
```

###### V2: Change input argument to `act1` 

1. Update the `application.yaml` to use `MyWorkflowImplV2` instead of `MyWorkflowImplV1`.
2. Rerun the Worker application.

```sh
temporal workflow query \
  --name getExecutionDetails \
  --workflow-id a1__v1
```

*Observe*: No NDE because no Versioning required; of course, only future executions would honor this change to activity input. 

*Reason*: Changing command (input) arguments do not cause an Non-Determinism Exception (NDE) during Replay.

*Fix*: N/A


###### V3: Add `act2` after first `act1` invocation

1. Update the `application.yaml` to use `MyWorkflowImplV3` instead of `MyWorkflowImplV2`.
2. Rerun the Worker application.

*Observe*
1. Run the `getExecutionDetails` query
2. See the `io.temporal.worker.NonDeterministicException`

*Reason*: Inserting a command (here, an Activity `act2`) conflicts with the event history, so Replay will fail.

*Fix*: 
1. Wrap the `act2` invocation inside a `GetVersion` result

*Observe*
1. Run the `getExecutionDetails` query
2. See the value returned, instead of an `io.temporal.worker.NonDeterministicException`
3. Start a new Workflow execution with a different ID
4. See the `act2` Activity is executed inside the new Version of the Workflow

*Reason*: 
1. Since we did not mix both Versioned and unVersioned changes inside the deployment, we could safely repair our
Workflow code and redeploy.
2. New executions _only_ will pick up this change, as expected.

###### V4: Add `act2` inside each iteration of the loop just after the `act1` invocation

Code changes that appear in a loop can EITHER be Versioned with a `changeId` for the entire loop OR per iteration.
Should we memoize or not with call to GetVersion. Typescript is a snowflake.

To version the entire loop, simply call `GetVersion` with the same `changeId` and wrap code as previously.

The entire loopsimply means checking the `maxVersion` returned from `GetVersion` inside each iteration.

Each iteration can be Versioned means using a unique `changeId` per iteration; for example, suffixing the 
change tag with an index.

Which you choose depends on these criteria:
CHAD: Am I ok with all iterations having the same code? This is answered by the problem of time.

_Bounded loops_
Define "Bounded" ... it might include continue/break or it might be upper bound of an incrementer


If your loop is bounded:
* AND _each iteration_ is quickly executed 
  * consider wrapping the entire loop containing your changes withing a single `changeId`. 
* AND _each iteration_ might block
  * wrap each iteration's change with a `changeId` that is unique to the iteration (eg, suffixed with an index)
  * CHADTODO: What is the value of a per-iteration changeId if it is bounded other than mid-loop deployments?
  * Callout RISK: Of SearchAttribute blowing up with all the versions being recorded
  
If your loop is _un_bounded:
* wrap each iteration's change with a `changeId` that is unique to the iteration (eg, suffixed with an index)
* CHAD: 



*Observe*
1. Run the `getValue` query
2. See the `io.temporal.worker.NonDeterministicException`
3. Match the `EventId` referenced in the stack trace to the history entry just after the first iteration of the loop

*Reason*: I


