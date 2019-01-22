package org.openlmis.contract_tests.hapifhir;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.concurrent.TimeUnit;

public class SubscriptionStepDefs {

  private SubscriptionTestHelper subscriptionTestHelper;

  @Before("@SubscriptionTests")
  public void setup() {
    subscriptionTestHelper = new SubscriptionTestHelper();
  }

  @Given("^I have an upstream FHIR server$")
  public void stubAMockServer() {
    subscriptionTestHelper.stubForLocation();
  }

  @When("^my upstream FHIR server subscribes to Location updates with the OpenLMIS FHIR Service$")
  public void createASubscription(String body) throws Exception {
    subscriptionTestHelper.createAFHIRSubscriptionResource(body);
  }

  @When("^I update an OpenLMIS Location")
  public void createAHapiLocation(String body) throws Exception {
    subscriptionTestHelper.createFHIRLocationResource(body);
  }

  @Then("^I verify that my Upstream FHIR Server has received a notification of a Location change$")
  public void verifyThatMockServerIsCalled() {
    subscriptionTestHelper.verifyThatStubWasCalled();
  }

}
