package org.openlmis.contract_tests.hapifhir;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openlmis.contract_tests.common.JsonFieldHelper;

public class LocationStepDefs {

  private final Map<String, TestHelper> helpers = new HashMap<>();

  private Response response;

  private String resourceName;
  private JSONObject resourceAsJson;

  @Before("@LocationTests")
  public void setUp() {
    helpers.put(TestHelper.FACILITY, new FacilityTestHelper());
    helpers.put(TestHelper.GEO_ZONE, new GeographicZoneTestHelper());
    helpers.put(TestHelper.LOCATION, new LocationTestHelper());
  }

  @When("^I create (.*):$")
  public void tryCreateResource(String resource, String bodyAsString) throws ParseException {
    response = findHelper(resource).createResource(bodyAsString);
  }

  @Then("^(.*) should be created$")
  public void shouldCreateResource(String resource) throws ParseException {
    ValidatableResponse response = this.response
        .then()
        .statusCode(HttpStatus.SC_CREATED);

    resourceName = resource;
    resourceAsJson = findHelper(resource).getResource(response);
  }

  @Then("^related (.*) should also be created$")
  public void shouldCreateFhirResource(String resource) {
    Object resourceId = resourceAsJson.get("id");
    findHelper(resourceName).verifyFhirResourceAfterCreate(resource, resourceId);
  }

  @When("^I update (.*):$")
  public void tryUpdateResource(String resource, DataTable table) throws ParseException {
    Object resourceId = resourceAsJson.get("id");

    table
        .asMaps()
        .stream()
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .forEach(entry -> JsonFieldHelper
            .setField(resourceAsJson, entry.getKey(), entry.getValue()));

    response = findHelper(resource).updateResource(resourceId, resourceAsJson.toString());
  }

  @Then("^(.*) should be up-to-date$")
  public void shouldUpdateResource(String resource) throws ParseException {
    ValidatableResponse response = this.response
        .then()
        .statusCode(HttpStatus.SC_OK);

    resourceName = resource;
    resourceAsJson = findHelper(resource).getResource(response);
  }

  @Then("^related (.*) should also be up-to-date")
  public void shouldUpdateFhirLocation(String resource) {
    Object resourceId = resourceAsJson.get("id");
    findHelper(resourceName).verifyFhirResourceAfterUpdate(resource, resourceId);
  }

  @When("^I try to find (.*) for (.*):$")
  public void tryToFindResource(String findResourceName, String resource,
      DataTable parameters) {
    Map<String, String> params = parameters
        .asMaps()
        .stream()
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    this.resourceName = resource;
    findHelper(resource).findAndSaveResource(findResourceName, params);
  }

  @Then("^I should find (.*)$")
  public void shouldFindResource(String resource) throws ParseException {
    findHelper(this.resourceName).verifyFoundResource(resource);
  }

  @When("^I use (.*) field of found (.*) to set (.*) field in (.*)$")
  public void shouldSetField(String fieldInFound, String resourceName, String propertyPath,
      String newResourceName) {
    findHelper(newResourceName).saveTransformation(resourceName, fieldInFound, propertyPath);

  }

  private TestHelper findHelper(String resource) {
    return Optional
        .ofNullable(helpers.get(resource))
        .orElseThrow(() -> new IllegalStateException("Unsupported resource: " + resource));
  }

}
