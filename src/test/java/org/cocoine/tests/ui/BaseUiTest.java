package org.cocoine.tests.ui;

import org.cocoine.framework.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUiTest {
  protected WebDriver driver;

  @BeforeMethod(alwaysRun = true)
  public void startBrowser(ITestResult result) {
    driver = DriverManager.initDriver();
    result.getTestContext().setAttribute("driver", driver);
  }
}
