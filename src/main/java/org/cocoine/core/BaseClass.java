package org.cocoine.core;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseClass {
  protected WebDriver driver;

  @BeforeMethod(alwaysRun = true)
  public void startBrowser(ITestContext context) {
    driver = createDriver();
    driver.manage().window().maximize();
    context.setAttribute("driver", driver);
  }

  @AfterMethod(alwaysRun = true)
  public void closeBrowser() {
    if (driver != null) {
      driver.quit();
      driver = null;
    }
  }

  public WebDriver getDriver() {
    return driver;
  }

  public Map<String, String> getLocators(String filePath) {
    return ResourceHelper.readStringMap(filePath);
  }

  public Iterator<Object[]> getJsonIterator(String filePath, String key) {
    JsonNode records = ResourceHelper.readJson(filePath).path(key);
    List<Object[]> data = new ArrayList<>();
    records.forEach(record -> data.add(new Object[] {record}));
    return data.iterator();
  }

  public WebElement waitForVisible(By locator) {
    return driverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public WebElement waitForClickable(By locator) {
    return driverWait().until(ExpectedConditions.elementToBeClickable(locator));
  }

  public void click(By locator) {
    waitForClickable(locator).click();
  }

  public void clickByJavascript(By locator) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", waitForVisible(locator));
  }

  public void setText(By locator, String value) {
    WebElement element = waitForVisible(locator);
    element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
    element.sendKeys(Keys.BACK_SPACE);
    element.sendKeys(value);
  }

  public String getText(By locator) {
    WebElement element = waitForVisible(locator);
    String text = element.getText();
    return text == null || text.isBlank() ? element.getAttribute("value") : text;
  }

  public boolean isVisible(By locator) {
    try {
      return waitForVisible(locator).isDisplayed();
    } catch (RuntimeException e) {
      return false;
    }
  }

  public void selectByVisibleText(By locator, String text) {
    new Select(waitForVisible(locator)).selectByVisibleText(text);
  }

  public String captureScreenshot() {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
  }

  protected By xpath(String locator, String... params) {
    return By.xpath(replaceParams(locator, params));
  }

  protected String replaceParams(String locator, String... params) {
    String resolved = locator;
    for (String param : params) {
      resolved = resolved.replaceFirst("<<param>>", param);
    }
    return resolved;
  }

  private WebDriverWait driverWait() {
    return new WebDriverWait(driver, Duration.ofSeconds(Config.timeoutSeconds()));
  }

  private WebDriver createDriver() {
    String browser = Config.browser().toLowerCase();
    if (Config.remote()) {
      try {
        if ("firefox".equals(browser)) {
          return new RemoteWebDriver(new URL(Config.gridUrl()), firefoxOptions());
        }
        return new RemoteWebDriver(new URL(Config.gridUrl()), chromeOptions());
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException("Invalid Selenium Grid URL: " + Config.gridUrl(), e);
      }
    }
    if ("firefox".equals(browser)) {
      return new FirefoxDriver(firefoxOptions());
    }
    return new ChromeDriver(chromeOptions());
  }

  private ChromeOptions chromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-notifications", "--remote-allow-origins=*");
    if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
      options.addArguments("--headless=new", "--window-size=1920,1080");
    }
    return options;
  }

  private FirefoxOptions firefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments("--disable-notifications");
    if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
      options.addArguments("--headless");
    }
    return options;
  }
}
