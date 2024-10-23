package io.temporal.closer.versioning.workflows;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkflowsUtils {
  public static Pattern PATTERN_VERSION =
      Pattern.compile("__(?<version>V\\d+)$", Pattern.CASE_INSENSITIVE);

  public static String getRootVersion(String workflowId, Version version) {
    Matcher m = PATTERN_VERSION.matcher(workflowId.toLowerCase());
    if (!Objects.isNull(version)) {
      return version.root();
    }
    while (m.find()) {
      var rootVersion = m.group("version");
      if (Objects.nonNull(rootVersion)) {
        return rootVersion.toUpperCase();
      }
    }

    return null;
  }
  //  public static MyWorkflowParams enrichParams(WorkflowInfo workflowInfo, MyWorkflowParams params
  // ) {
  //    var rootVersion = getRootVersion(workflowInfo.getWorkflowId(), params.version());
  //    if(Objects.isNull(rootVersion)) {
  //    }
  //    if (rootVersion != null) {
  //
  //    }
  //  }
}
