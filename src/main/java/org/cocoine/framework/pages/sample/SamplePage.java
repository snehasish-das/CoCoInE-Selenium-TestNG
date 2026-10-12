package org.cocoine.framework.pages.sample;

import org.cocoine.framework.ui.LocatorRepository;
import org.cocoine.framework.ui.SeleniumActions;
import org.openqa.selenium.WebDriver;

public class SamplePage extends SeleniumActions {
  private final LocatorRepository locators = new LocatorRepository("sample", "sample-page");

  public SamplePage(WebDriver driver) {
    super(driver);
  }

  public SamplePage open(String url) {
    driver.get(url);
    return this;
  }

  public SamplePage search(String text) {
    setInputText(locators.by("searchInput"), text);
    return this;
  }

  public String heading() {
    return readText(locators.by("heading"));
  }
}
