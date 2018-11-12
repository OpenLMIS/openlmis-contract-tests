package org.openlmis.contract_tests.hapifhir;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.concurrent.TimeUnit;

public class SubscriptionStepDefs {

  LocationTestHelper locationTestHelper;

  SubscriptionTestHelper subscriptionTestHelper;

  @Before("@SubscriptionTests")
  public void setup() {
    locationTestHelper = new LocationTestHelper();
    subscriptionTestHelper = new SubscriptionTestHelper();
  }

  @And("^I stub a mock server$")
  public void stubAMockServer() {
    subscriptionTestHelper.stubForLocation();
  }

  @When("^I post a rest-hook subscription resource to call wiremock server on any location update$")
  public void createASubscription(String body) throws Exception {
    subscriptionTestHelper.createAFHIRSubscriptionResource(body);
  }

  @Then("^I post a new hapifhir location")
  public void createAHapiLocation(String body) throws Exception {
    subscriptionTestHelper.createFHIRLocationResource(body);
  }

  @Then("^verify that mock server is called$")
  public void verifyThatMockServerIsCalled() {
    subscriptionTestHelper.verifyThatStubWasCalled();
  }

  @Then("^Wait for (\\d+) seconds$")
  public void waitFor(int seconds) throws Exception {
    TimeUnit.SECONDS.sleep(seconds);
  }

}
