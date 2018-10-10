package org.openlmis.contract_tests.hapifhir;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openlmis.contract_tests.common.JsonFieldSetter;

public class LocationStepDefs {

  private static final String FACILITY = "facility";
  private static final String GEO_ZONE = "geographic zone";
  private static final String LOCATION = "location";

  private static final Map<String, TestHelper> HELPERS;

  static {
    HELPERS = new HashMap<>();
    HELPERS.put(FACILITY, new FacilityTestHelper());
    HELPERS.put(GEO_ZONE, new GeographicZoneTestHelper());
    HELPERS.put(LOCATION, new LocationTestHelper());
  }

  private Response response;

  private String resourceName;
  private JSONObject resourceAsJson;

  @When("^I create a (facility|geographic zone|location):$")
  public void tryCreateResource(String resource, String bodyAsString) {
    response = findHelper(resource).createResource(bodyAsString);
  }

  @Then("^the (facility|geographic zone|location) should be created$")
  public void shouldCreateResource(String resource) throws ParseException {
    ValidatableResponse response = this.response
        .then()
        .statusCode(HttpStatus.SC_CREATED);

    resourceName = resource;
    resourceAsJson = findHelper(resource).getResource(response);
  }

  @Then("^the related (facility|geographic zone|location) should be created$")
  public void shouldCreateFhirResource(String resource) {
    Object resourceId = resourceAsJson.get("id");
    findHelper(resourceName).verifyFhirResourceAfterCreate(resource, resourceId);
  }

  @When("^I update the (facility|geographic zone|location):$")
  public void tryUpdateResource(String resource, DataTable table) {
    Object resourceId = resourceAsJson.get("id");

    table
        .asMaps()
        .stream()
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .forEach(entry -> JsonFieldSetter
            .setField(resourceAsJson, entry.getKey(), entry.getValue()));

    response = findHelper(resource).updateResource(resourceId, resourceAsJson.toString());
  }

  @Then("^the (facility|geographic zone|location) should be up-to-date$")
  public void shouldUpdateResource(String resource) throws ParseException {
    ValidatableResponse response = this.response
        .then()
        .statusCode(HttpStatus.SC_OK);

    resourceName = resource;
    resourceAsJson = findHelper(resource).getResource(response);
  }

  @Then("^the related (facility|geographic zone|location) should be updated$")
  public void shouldUpdateFhirLocation(String resource) {
    Object resourceId = resourceAsJson.get("id");
    findHelper(resourceName).verifyFhirResourceAfterUpdate(resource, resourceId);
  }

  private TestHelper findHelper(String resource) {
    return Optional
        .ofNullable(HELPERS.get(resource))
        .orElseThrow(() -> new IllegalStateException("Unsupported resource: " + resource));
  }

}
