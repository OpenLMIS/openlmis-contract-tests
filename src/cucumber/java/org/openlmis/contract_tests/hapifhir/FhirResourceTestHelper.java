package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class FhirResourceTestHelper extends BaseTestHelper {

  @Override
  public JSONObject getResource(ValidatableResponse response)
      throws ParseException {
    String href = response
        .extract()
        .header(HttpHeaders.CONTENT_LOCATION);

    String locationAsString = given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1")
        .when()
        .get(href)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .asString();

    return (JSONObject) new JSONParser().parse(locationAsString);
  }

}
