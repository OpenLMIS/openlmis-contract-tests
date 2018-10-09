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

class GeographicZoneTestHelper implements TestHelper {

  private static final String BASE_REFERENCE_DATA_URL = baseUrlOfService("referencedata");
  private static final String GEO_ZONE_URL = BASE_REFERENCE_DATA_URL + "geographicZones";

  private static final String PHYSICAL_TYPE = "area";

  private static final String CODE = "CT-HAPI-FHIR-GZ";
  private static final String NAME = "Contract Test HAPI FHIR Geographic Zone";

  private static final String LEVEL_ID = "90e35999-a64f-4312-ba8f-bc13a1311c75";
  private static final String PARENT_ID = "4e471242-da63-436c-8157-ade3e615c848";

  private static final int LATITUDE_BEFORE = 0;
  private static final int LATITUDE_AFTER = 2;

  private static final int LONGITUDE_BEFORE = 0;
  private static final int LONGITUDE_AFTER = 2;

  @Override
  public Response createResource(String bodyAsString) {
    return given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyAsString)
        .when()
        .post(GEO_ZONE_URL);
  }

  @Override
  public void verifyLocationAfterCreate(ValidatableResponse response) {
    verifyLocation(response, LATITUDE_BEFORE, LONGITUDE_BEFORE);
  }

  @Override
  public Response updateResource(Object resourceId, String bodyAsString) {
    return given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyAsString)
        .when()
        .put(GEO_ZONE_URL + "/" + resourceId);
  }

  @Override
  public void verifyLocationAfterUpdate(ValidatableResponse response) {
    verifyLocation(response, LATITUDE_AFTER, LONGITUDE_AFTER);
  }

  private void verifyLocation(ValidatableResponse response, int latitude, int longitude) {
    response
        .body("alias", hasItem(CODE))
        .body("name", is(NAME))
        .body("identifier.value", hasItem(endsWith(LEVEL_ID)))
        .body("partOf.reference", endsWith(PARENT_ID))
        .body("position.latitude", is(latitude))
        .body("position.longitude", is(longitude))
        .body("physicalType.coding.code", hasItem(PHYSICAL_TYPE));
  }

}
