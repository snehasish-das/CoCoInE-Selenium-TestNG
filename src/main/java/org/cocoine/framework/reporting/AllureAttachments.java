package org.cocoine.framework.reporting;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class AllureAttachments {
  private AllureAttachments() {
  }

  @Attachment(value = "{name}", type = "image/png")
  public static byte[] screenshot(String name, WebDriver driver) {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  }

  @Attachment(value = "{name}", type = "text/plain")
  public static String text(String name, String content) {
    return content;
  }
}
