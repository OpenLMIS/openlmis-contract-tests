package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

public class LocationStepDefs {

  private static final String BASE_HAPI_FHIR_URL = baseUrlOfService("hapifhir");
  private static final String LOCATION_URL = BASE_HAPI_FHIR_URL + "Location";

  private static final String FACILITY = "facility";
  private static final String GEO_ZONE = "geographic zone";

  private static final String SEARCH_BY_IDENTIFIER_FORMAT = "%s|%s";
  private static final String BASE_URL = System.getenv("BASE_URL");

  private static final Map<String, TestHelper> HELPERS;

  static {
    HELPERS = new HashMap<>();
    HELPERS.put(FACILITY, new FacilityTestHelper());
    HELPERS.put(GEO_ZONE, new GeographicZoneTestHelper());
  }

  private Response response;

  private String resourceName;
  private JSONObject resourceAsJson;

  @When("^I create a (facility|geographic zone):$")
  public void tryCreateResource(String resource, String bodyAsString) {
    response = findHelper(resource).createResource(bodyAsString);
  }

  @Then("^the (facility|geographic zone) should be created$")
  public void shouldCreateResource(String resource) {
    String resultAsString = response
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract()
        .asString();

    resourceName = resource;
    resourceAsJson = new JSONObject(resultAsString);
  }

  @Then("^the related FHIR location should be created$")
  public void shouldCreateFhirLocation() {
    Object resourceId = resourceAsJson.get("id");

    String queryParamValue = String.format(
        SEARCH_BY_IDENTIFIER_FORMAT,
        BASE_URL, resourceId
    );

    ValidatableResponse response = given()
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1")
        .queryParam("identifier", queryParamValue)
        .when()
        .get(LOCATION_URL)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("entry", hasSize(1))
        .root("entry[0]")
        .body("resource", is(notNullValue()))
        .body("resource.meta.versionId", is("1"))
        .root("entry[0].resource");

    findHelper(resourceName).verifyLocationAfterCreate(response);
  }

  @When("^I update the (facility|geographic zone):$")
  public void tryUpdateResource(String resource, DataTable table) {
    Object resourceId = resourceAsJson.get("id");

    table
        .asMaps()
        .stream()
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .forEach(entry -> resourceAsJson.put(entry.getKey(), entry.getValue()));

    response = findHelper(resource).updateResource(resourceId, resourceAsJson.toString());
  }

  @Then("^the (facility|geographic zone) should be up-to-date$")
  public void shouldUpdateResource(String resource) {
    String resultAsString = response
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .asString();

    resourceName = resource;
    resourceAsJson = new JSONObject(resultAsString);
  }

  @Then("^the related FHIR location should be updated$")
  public void shouldUpdateFhirLocation() {
    Object resourceId = resourceAsJson.get("id");

    String queryParamValue = String.format(
        SEARCH_BY_IDENTIFIER_FORMAT,
        BASE_URL, resourceId
    );

    ValidatableResponse response = given()
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1")
        .queryParam("identifier", queryParamValue)
        .when()
        .get(LOCATION_URL)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("entry", hasSize(1))
        .root("entry[0]")
        .body("resource", is(notNullValue()))
        .body("resource.meta.versionId", is("2"))
        .root("entry[0].resource");

    findHelper(resourceName).verifyLocationAfterUpdate(response);
  }

  private TestHelper findHelper(String resource) {
    return Optional
        .ofNullable(HELPERS.get(resource))
        .orElseThrow(() -> new IllegalStateException("Unsupported resource: " + resource));
  }

}
