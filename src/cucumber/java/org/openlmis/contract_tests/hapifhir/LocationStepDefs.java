package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Collection;
import java.util.Map;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

public class LocationStepDefs {

  private static final String BASE_REFERENCE_DATA_URL = baseUrlOfService("referencedata");
  private static final String FACILITY_URL = BASE_REFERENCE_DATA_URL + "facilities";
  private static final String GEO_ZONE_URL = BASE_REFERENCE_DATA_URL + "geographicZones";

  private static final String BASE_HAPI_FHIR_URL = baseUrlOfService("hapifhir");
  private static final String LOCATION_URL = BASE_HAPI_FHIR_URL + "Location";

  private static final String FACILITY = "facility";
  private static final String GEO_ZONE = "geographic zone";

  private static final String SEARCH_BY_IDENTIFIER_FORMAT = "%s|%s";
  private static final String BASE_URL = System.getenv("BASE_URL");

  private Response response;
  private JSONObject resourceAsJson;

  @When("^I create a (facility|geographic zone):$")
  public void tryCreateResource(String resource, String bodyAsString) {
    RequestSpecification specification = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyAsString)
        .when();

    switch (resource) {
      case FACILITY:
        response = specification.post(FACILITY_URL);
        break;
      case GEO_ZONE:
        response = specification.post(GEO_ZONE_URL);
        break;
      default:
        throw new IllegalStateException("Unsupported resource: " + resource);
    }
  }

  @Then("^the (facility|geographic zone) should be created$")
  public void shouldCreateResource(String resource) {
    String resultAsString = response
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract()
        .asString();

    resourceAsJson = new JSONObject(resultAsString);
  }

  @Then("^the related FHIR location should be created$")
  public void shouldCreateFhirLocation() {
    Object resourceId = resourceAsJson.get("id");

    String queryParamValue = String.format(
        SEARCH_BY_IDENTIFIER_FORMAT,
        BASE_URL, resourceId
    );

    given()
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1")
        .queryParam("identifier", queryParamValue)
        .when()
        .get(LOCATION_URL)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("entry", hasSize(1))
        .body("entry[0].resource", is(notNullValue()))
        .body("entry[0].resource.meta.versionId", is("1"));
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

    RequestSpecification specification = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(resourceAsJson.toString())
        .when();

    switch (resource) {
      case FACILITY:
        response = specification.put(FACILITY_URL + "/" + resourceId);
        break;
      case GEO_ZONE:
        response = specification.put(GEO_ZONE_URL + "/" + resourceId);
        break;
      default:
        throw new IllegalStateException("Unsupported resource: " + resource);
    }
  }

  @Then("^the (facility|geographic zone) should be up-to-date$")
  public void shouldUpdateResource(String resource) {
    String resultAsString = response
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .asString();

    resourceAsJson = new JSONObject(resultAsString);
  }

  @Then("^the related FHIR location should be updated$")
  public void shouldUpdateFhirLocation() {
    Object resourceId = resourceAsJson.get("id");

    String queryParamValue = String.format(
        SEARCH_BY_IDENTIFIER_FORMAT,
        BASE_URL, resourceId
    );

    given()
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1")
        .queryParam("identifier", queryParamValue)
        .when()
        .get(LOCATION_URL)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("entry", hasSize(1))
        .body("entry[0].resource", is(notNullValue()))
        .body("entry[0].resource.meta.versionId", is("2"));
  }

}
