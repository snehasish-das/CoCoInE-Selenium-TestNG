package org.cocoine.framework.execution;

import io.qameta.allure.Allure;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.cocoine.framework.logging.FrameworkLogger;

public final class PolicyGatedFlow {
  private final String flowName;
  private final List<FlowStep> steps = new ArrayList<>();

  private PolicyGatedFlow(String flowName) {
    this.flowName = flowName;
  }

  public static PolicyGatedFlow flow(String flowName) {
    return new PolicyGatedFlow(flowName);
  }

  public PolicyGatedFlow step(int number, String name, ThrowingRunnable action) {
    steps.add(new FlowStep(number, name, action));
    return this;
  }

  public void run() {
    int maxAllowedStep = TestExecutionPolicy.maxAllowedStep(flowName);
    int executed = 0;

    List<FlowStep> sortedSteps = steps.stream()
        .sorted(Comparator.comparingInt(FlowStep::number))
        .collect(Collectors.toList());

    for (FlowStep step : sortedSteps) {
      if (!TestExecutionPolicy.canRunStep(flowName, step.number())) {
        String message = "Policy skipped flow '" + flowName + "' from step " + step.number()
            + " because max allowed step is " + maxAllowedStep + ".";
        FrameworkLogger.info(message);
        Allure.addAttachment("policy-gated-flow-boundary", message);
        break;
      }

      String stepLabel = "Step " + step.number() + " - " + step.name();
      FrameworkLogger.info("Running " + flowName + ": " + stepLabel);
      Allure.step(stepLabel, () -> step.action().run());
      executed++;
    }

    String summary = "Flow '" + flowName + "' executed " + executed + " of " + steps.size()
        + " configured steps in env '" + org.cocoine.framework.config.FrameworkConfig.environment() + "'.";
    FrameworkLogger.info(summary);
    Allure.addAttachment("policy-gated-flow-summary", summary);
  }

  @FunctionalInterface
  public interface ThrowingRunnable {
    void run() throws Exception;
  }

  private static final class FlowStep {
    private final int number;
    private final String name;
    private final ThrowingRunnable action;

    private FlowStep(int number, String name, ThrowingRunnable action) {
      this.number = number;
      this.name = name;
      this.action = action;
    }

    private int number() {
      return number;
    }

    private String name() {
      return name;
    }

    private ThrowingRunnable action() {
      return action;
    }
  }
}
