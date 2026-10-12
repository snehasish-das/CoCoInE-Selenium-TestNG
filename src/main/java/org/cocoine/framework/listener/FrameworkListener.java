package org.cocoine.framework.listener;

import io.qameta.allure.Allure;
import org.cocoine.framework.driver.DriverManager;
import org.cocoine.framework.reporting.AllureAttachments;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class FrameworkListener implements ITestListener {
  @Override
  public void onTestStart(ITestResult result) {
    Allure.label("owner", System.getProperty("owner", "qa-automation"));
    for (String group : result.getMethod().getGroups()) {
      Allure.label("tag", group);
    }
  }

  @Override
  public void onTestFailure(ITestResult result) {
    attachScreenshot("failure-screenshot");
    AllureAttachments.text("failure-message", String.valueOf(result.getThrowable()));
    DriverManager.quitDriver();
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    if (Boolean.parseBoolean(System.getProperty("capture.screenshot.on.pass", "false"))) {
      attachScreenshot("pass-screenshot");
    }
    DriverManager.quitDriver();
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    DriverManager.quitDriver();
  }

  @Override
  public void onFinish(ITestContext context) {
    DriverManager.quitDriver();
  }

  private void attachScreenshot(String name) {
    try {
      WebDriver driver = DriverManager.getDriver();
      AllureAttachments.screenshot(name, driver);
    } catch (RuntimeException ignored) {
      AllureAttachments.text(name, "No active WebDriver was available.");
    }
  }
}
