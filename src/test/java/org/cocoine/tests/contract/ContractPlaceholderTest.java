package org.cocoine.tests.contract;

import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ContractPlaceholderTest {
  @Feature("Contract")
  @Test(groups = {"contract"}, description = "Placeholder proving contract suite wiring")
  public void contractSuiteIsWired() {
    Assert.assertTrue(true, "Replace with Pact or Spring Cloud Contract provider verification.");
  }
}
