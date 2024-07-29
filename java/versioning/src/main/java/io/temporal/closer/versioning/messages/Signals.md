# Signals

## Signal Input Messages

This study shows what happens if parameter names change for Signal input messages.

### Demonstration

* Workflow Type: [VersioningWorkflow](VersioningWorkflow.java)
* Workflow Implementation: [SignalsDemoImpl](SignalsDemoImpl.java)

1. Run `Application`
2. Start workflow with:
```
temporal workflow start \
    --type VersioningWorkflow \
    --task-queue messages \
    --workflow-id vw1 \
    --input '{"id": "vw1", "name": "bob smith"}' 
```
3. Signal workflow with `execute`:
```
temporal workflow signal \
    --workflow-id vw1 \
    --name execute \
    --input '{"name": "bob smith"}'
```
4. Stop `Application`
5. Rename `SignalInputRequest.java` property `name` to `fullName`
6. Run `Application`
7. Query workflow with `getSignalInput`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name signalInput
```
8. _Observe_ that 
   1. The query did not ERROR but it _cannot_ return the expected value you passed in previously.
   2. There is _not_ a `WorkflowTaskFailed` in your application history by doing `temporal workflow show --event-details --workflow-id vw1`
   3. There _is_ a `DataConverterException` in your logs complaining about an unrecognized `name` field 
9. Signal workflow again with `execute`:
```
temporal workflow signal \
    --workflow-id vw1 \
    --name execute \
    --input '{"fullName": "bob barker"}'
```
10. _Observe_ you now have two `WorkflowExecutionSignaled` events in your history but with _different inputs_
```text
temporal workflow show \
    --workflow-id vw1 \
    --event-details
```
11. Stop the `Application` again and rename `SignalInputRequest.java` property `fullName` to `name`
12. Run `Application` and Query workflow with `getSignalInput`:
```
temporal workflow query \
    --workflow-id vw1 \
    --name getSignalInput
```
13. _Observe_ the FIRST value you Signaled with is returned, and you have the same deserialization errors in your logs
14. Stop `Application`

### Explanation

> Remember: Temporal will accept _any_ Signal with _any_ input values passed to an Workflow Execution.

Note that our Execution history right now has _two_ properties carrying values we "care" about and 
that Temporal will not loudly fail a Task that experiences DataConverterExceptions like these.
Unlike the Workflow Task Failures we witnessed in Execution history when making changes to Activity _results_, the subtle changes to
Signal _inputs_ which can just as easily impact the truth our Workflow represents is not so evident.


This makes **validation** of Signals before sending them and **versioning** their message schemas even more crucial.

### Cautions / Conclusions

This underscores the urgent need for even your _input_ messages to be Versioned! 
Note that consumers of an Execution `Query` could receive stale data if your inputs are affecting Workflow business state (variables) and
_not be aware that the Execution has failed to update the "truth" due to Serialization exceptions_. 

This also highlights the need to monitor closely for any `DataConverterException` logs in your Temporal Workers to guard against invalid data
impacting your User Experiences.