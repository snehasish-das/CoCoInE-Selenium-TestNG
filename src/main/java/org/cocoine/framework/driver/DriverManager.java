package org.cocoine.framework.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.cocoine.framework.config.FrameworkConfig;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.util.logging.Level;

public final class DriverManager {
  private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

  private DriverManager() {
  }

  public static WebDriver getDriver() {
    WebDriver driver = DRIVER.get();
    if (driver == null) {
      throw new IllegalStateException("WebDriver has not been initialized for this thread.");
    }
    return driver;
  }

  public static WebDriver initDriver() {
    WebDriver driver = createDriver();
    driver.manage().window().maximize();
    DRIVER.set(driver);
    return driver;
  }

  public static void quitDriver() {
    WebDriver driver = DRIVER.get();
    if (driver != null) {
      driver.quit();
      DRIVER.remove();
    }
  }

  private static WebDriver createDriver() {
    String browser = FrameworkConfig.browser().toLowerCase();
    MutableCapabilities options = "firefox".equals(browser) ? firefoxOptions() : chromeOptions();
    if (FrameworkConfig.remote()) {
      try {
        return new RemoteWebDriver(new URL(FrameworkConfig.gridUrl()), options);
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException("Invalid Selenium Grid URL: " + FrameworkConfig.gridUrl(), e);
      }
    }
    if ("firefox".equals(browser)) {
      return new FirefoxDriver((FirefoxOptions) options);
    }
    return new ChromeDriver((ChromeOptions) options);
  }

  private static ChromeOptions chromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-notifications", "--remote-allow-origins=*");
    if ("dark".equalsIgnoreCase(System.getProperty("colorScheme", ""))) {
      options.addArguments("--force-dark-mode");
    }
    LoggingPreferences logs = new LoggingPreferences();
    logs.enable(LogType.PERFORMANCE, Level.ALL);
    logs.enable(LogType.BROWSER, Level.ALL);
    options.setCapability("goog:loggingPrefs", logs);
    return options;
  }

  private static FirefoxOptions firefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();
    Map<String, Object> prefs = new HashMap<>();
    prefs.put("dom.webnotifications.enabled", false);
    options.setCapability("moz:firefoxOptions", Map.of("prefs", prefs));
    return options;
  }
}
