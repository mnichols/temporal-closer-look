# Versioning: Messages

## Demonstrations 
1. [Workflows](Workflows.md)
2. [Activities](Activities.md)
3. [Signals](Signals.md)

> When running these Demos, note that a redeployment with corrected code might take a while to take effect due to Task backoff rules.
> Don't be alarmed if your corrected code does not immediately take effect.
> Workflow Task Retry Interval is configured at the Temporal Server (is not an SDK property) and is
> [10 minutes by default](https://github.com/temporalio/temporal/blob/9450b41489d21b45fa33576fc9dec61963a3deed/common/dynamicconfig/constants.go#L1963).
> That means it may take up to 10 minutes for a redeploy to take effect on transient failures.

### Lessons

Have a plan for how to make changes to your input AND output messages used in Temporal primitives.
Temporal Workflow executions are replayed an arbitrary number of times so always keep the "replay" in mind.

### Options For Workflow Message Versioning

1. Use a messaging protocol that has versioning baked in, like [protobufs](https://protobuf.dev/programming-guides/proto3/).
2. Adopt evolutionary versioning for your messaging.
   1. Prefer only _additive_ changes to your contracts instead of breaking changes. For example, renaming a property is done by _adding_ the property to the existing message.
   2. [Here](https://medium.com/expedia-group-tech/handling-incompatible-schema-changes-with-avro-2bc147e26770) is a pretty good article describing the challenges.
3. Hook directly into serialization and deserialization of language types.
   1. (Java) Use Jackson's JSON [VersionedModelConverter](https://jonpeterson.github.io/docs/jackson-module-model-versioning/1.1.1/index.html?com/github/jonpeterson/jackson/module/versioning/JsonVersionedModel.html) to hook into serialization and deserialization 
      1. Here it is with [Spring](https://github.com/jonpeterson/spring-webmvc-model-versioning)
4. Use [PayloadConverter](https://www.javadoc.io/doc/io.temporal/temporal-sdk/latest/io/temporal/common/converter/PayloadConverter.html) to handle up/down translation to new versions

### Recommendations

1. Consider encapsulating messaging concerns into a discrete package. It is easier to track versioning and makes your API explicit.
2. Always use [Replay Testing](https://docs.temporal.io/develop/java/testing-suite#replay) in your pipeline to catch breaking changes before shipping.
