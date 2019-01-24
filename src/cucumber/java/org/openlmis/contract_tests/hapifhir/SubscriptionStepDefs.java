package org.openlmis.contract_tests.hapifhir;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import com.github.tomakehurst.wiremock.client.WireMock;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class SubscriptionStepDefs extends BaseHapiFhirStepDefs {

  private WireMock wiremock = WireMock.create().host("wiremock").port(8080).build();

  @Given("^I have an upstream FHIR server$")
  public void stubAMockServer() {
    wiremock.register(
        put(
            urlPathMatching("/fhir_locations/Location/.*"))
            .withHeader("Authorization", equalTo("Bearer 04199b94-15ce-4405-969c-05dedf4c073c"))
            .willReturn(aResponse().withStatus(200))
    );
  }

  @Then("^my upstream FHIR Server has received a notification of a location change$")
  public void verifyThatMockServerIsCalled() {
    wiremock.verifyThat(
        putRequestedFor(
            urlPathMatching("/fhir_locations/Location/.*"))
            .withHeader("Authorization", equalTo("Bearer 04199b94-15ce-4405-969c-05dedf4c073c"))
            .withRequestBody(matchingJsonPath("$.resourceType", containing("Location")))
            .withRequestBody(matchingJsonPath("$..name", containing("Contract Test S")))
            .withRequestBody(matchingJsonPath("$..alias", containing("CTS-FHIR-S"))));
  }

}
