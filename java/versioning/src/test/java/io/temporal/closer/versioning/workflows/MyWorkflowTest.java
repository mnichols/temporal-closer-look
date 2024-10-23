package io.temporal.closer.versioning.workflows;

import io.temporal.closer.versioning.DomainConfig;
import io.temporal.workflow.Workflow;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {
      MyWorkflowTest.Configuration.class,
    })
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@EnableAutoConfiguration()
@DirtiesContext
@ActiveProfiles("test")
@Import(DomainConfig.class)
public class MyWorkflowTest {
  private static class TestCase {
    String workflowId;
    Version version;
    String expected;

    TestCase(String workflowId, Version version, String expected) {
      this.workflowId = workflowId;
      this.version = version;
      this.expected = expected;
    }
  }

  @Test
  public void getRootVersion() {
    List<TestCase> cases =
        Arrays.asList(
            new TestCase("aa123__v2", null, "V2"),
            new TestCase("aa123", null, null),
            new TestCase("aa123__V3", new Version("V2", Workflow.DEFAULT_VERSION), "V2"));
    for (TestCase testCase : cases) {
      var actual = WorkflowsUtils.getRootVersion(testCase.workflowId, testCase.version);
      Assertions.assertEquals(testCase.expected, actual, testCase.workflowId);
    }
  }

  @ComponentScan
  public static class Configuration {}
}
