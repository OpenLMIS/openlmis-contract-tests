package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class OlmisResourceTestHelper extends BaseTestHelper {

  private static final String BASE_HAPI_FHIR_URL = baseUrlOfService("hapifhir");
  private static final String LOCATION_URL = BASE_HAPI_FHIR_URL + "Location";

  private static final String SEARCH_BY_IDENTIFIER_FORMAT = "%s|%s";
  private static final String BASE_URL = System.getenv("BASE_URL");

  @Override
  public JSONObject getResource(ValidatableResponse response)
      throws ParseException {
    return (JSONObject) new JSONParser().parse(response.extract().asString());
  }

  ValidatableResponse getLocation(Object resourceId, String versionId) {
    String queryParamValue = String.format(
        SEARCH_BY_IDENTIFIER_FORMAT,
        BASE_URL, resourceId
    );

    return given()
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
        .body("resource.meta.versionId", is(versionId))
        .root("entry[0].resource");
  }

}
