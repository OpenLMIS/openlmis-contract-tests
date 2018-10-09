package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

class FacilityTestHelper implements TestHelper {

  private static final String BASE_REFERENCE_DATA_URL = baseUrlOfService("referencedata");
  private static final String FACILITY_URL = BASE_REFERENCE_DATA_URL + "facilities";

  private static final String PHYSICAL_TYPE = "si";

  private static final String CODE = "CT-HAPI-FHIR-F";
  private static final String NAME = "Contract Test HAPI FHIR Facility";
  private static final String DESCRIPTION =
      "This is an example facility which should be added to FHIR server";

  private static final String PROGRAM_ID = "dce17f2e-af3e-40ad-8e00-3496adef44c3";
  private static final String GEO_ZONE_ID = "4df0cc89-8a71-450f-9c1a-29ceea1f14f3";
  private static final String TYPE_ID = "ac1d268b-ce10-455f-bf87-9c667da8f060";
  private static final String OPERATOR_ID = "9456c3e9-c4a6-4a28-9e08-47ceb16a4121";

  private static final String STATUS_BEFORE = "active";
  private static final String STATUS_AFTER = "inactive";


  @Override
  public Response createResource(String bodyAsString) {
    return given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyAsString)
        .when()
        .post(FACILITY_URL);
  }

  @Override
  public void verifyLocationAfterCreate(ValidatableResponse response) {
    verifyLocation(response, STATUS_BEFORE);
  }

  @Override
  public Response updateResource(Object resourceId, String bodyAsString) {
    return given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyAsString)
        .when()
        .put(FACILITY_URL + "/" + resourceId);
  }

  @Override
  public void verifyLocationAfterUpdate(ValidatableResponse response) {
    verifyLocation(response, STATUS_AFTER);
  }

  private void verifyLocation(ValidatableResponse response, String status) {
    response
        .body("alias", hasItem(CODE))
        .body("name", is(NAME))
        .body("description", is(DESCRIPTION))
        .body("physicalType.coding.code", hasItem(PHYSICAL_TYPE))
        .body(
            "identifier.value",
            hasItems(endsWith(PROGRAM_ID), endsWith(TYPE_ID), endsWith(OPERATOR_ID)))
        .body("partOf.reference", endsWith(GEO_ZONE_ID))
        .body("status", is(status));
  }

}
