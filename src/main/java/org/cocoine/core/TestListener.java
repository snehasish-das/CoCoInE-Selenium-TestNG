package org.cocoine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

  @Override
  public void onTestFailure(ITestResult result) {
    LOGGER.error("Test failed: {}", result.getName(), result.getThrowable());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    LOGGER.warn("Test skipped: {}", result.getName());
  }
}
