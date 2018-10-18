package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import java.util.Map;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

class FacilityTestHelper extends OlmisResourceTestHelper {

  private static final String PHYSICAL_TYPE = "si";

  private static final String CODE = "CT-F";
  private static final String NAME = "Contract Test Facility";
  private static final String DESCRIPTION =
      "This is an example facility which should be added to FHIR server";

  private static final String PROGRAM_ID = "dce17f2e-af3e-40ad-8e00-3496adef44c3";
  private static final String TYPE_ID = "ac1d268b-ce10-455f-bf87-9c667da8f060";
  private static final String OPERATOR_ID = "9456c3e9-c4a6-4a28-9e08-47ceb16a4121";

  private static final String GEO_ZONE_CODE = "CT-GZ-2";

  private static final String STATUS_BEFORE = "active";
  private static final String STATUS_AFTER = "inactive";

  @Override
  String getResourceUrl() {
    return FACILITY_URL;
  }

  @Override
  public void verifyFhirResourceAfterCreate(String resource, Object resourceId) {
    ValidatableResponse response = getLocation(resourceId, "1");
    verifyLocation(response, STATUS_BEFORE);
  }

  @Override
  public void verifyFhirResourceAfterUpdate(String resource, Object resourceId) {
    ValidatableResponse response = getLocation(resourceId, "2");
    verifyLocation(response, STATUS_AFTER);
  }

  private void verifyLocation(ValidatableResponse response, String status) {
    String resource = response
        .body("alias", hasItem(CODE))
        .body("name", is(NAME))
        .body("description", is(DESCRIPTION))
        .body("physicalType.coding.code", hasItem(PHYSICAL_TYPE))
        .body(
            "identifier.value",
            hasItems(endsWith(PROGRAM_ID), endsWith(TYPE_ID), endsWith(OPERATOR_ID)))
        .body("status", is(status))
        .extract()
        .asString();

    JsonPath from = from(resource);
    String reference = from.getString("entry[0].resource.partOf.reference");

    given()
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1")
        .when()
        .get(BASE_HAPI_FHIR_URL + reference)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("alias", hasItem(GEO_ZONE_CODE));
  }

}
