# Versioning: Workflows

This demonstrates an evolution of a Workflow definition called `MyWorkflow` that uses the _Patched_
Versioning strategy to make changes over time.

## How to simulate progressive changes

The `MyWorkflowProxyImpl` delegates to the implementation version you specify in the input
`version.root` parameter. 

For example, this input params will execute `MyWorkflowImplV2`:
```json
{ "value": "foo", "version": { "root":  "V2"}}
```

###### V1: Execute `MyWorkflow`

Use the following args to start a workflow execution.

```sh
temporal workflow start \
  --task-queue workflows \
  --type MyWorkflow \
  --workflow-id a1 \
  --input {args}
```

Where `args` is:

```json
{ "value": "foo", "version": {"root":  "V1"}}
```

###### V2: Change input argument to `act1` 

*Observe*: No Versioning required, but of course only future executions would not honor this change to activity input. 

*Reason*: Changing command (input) arguments do not cause an Non-Determinism Exception (NDE)

*Fix*: N/A

###### V3: Add `act2` after first `act1` invocation

*Observe*
1. Run the `getValue` query
2. See the `io.temporal.worker.NonDeterministicException`

*Reason*: Inserting a command (here, an Activity) conflicts with the event history, so Replay will fail.

*Fix*: 
1. Wrap the `act2` invocation inside a `GetVersion` result

*Observe*
1. Run the `getValue` query
2. See the value returned, instead of an `io.temporal.worker.NonDeterministicException`
3. Start a new Workflow execution with a different ID
4. See the `act2` activity is executed inside the new Version of the Workflow

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


