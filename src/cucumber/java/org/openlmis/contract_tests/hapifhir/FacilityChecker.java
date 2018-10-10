package org.openlmis.contract_tests.hapifhir;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.restassured.response.ValidatableResponse;

class FacilityChecker implements ResourceChecker {

  private static final String BASE_REFERENCE_DATA_URL = baseUrlOfService("referencedata");
  private static final String FACILITY_URL = BASE_REFERENCE_DATA_URL + "facilities";

  private static final String CODE = "CT-FHIR-F";
  private static final String NAME = "Contract Test FHIR Facility";
  private static final String DESCRIPTION =
      "This is an example location which should be added to the reference data service";

  private static final String GEO_ZONE_ID = "4df0cc89-8a71-450f-9c1a-29ceea1f14f3";
  private static final String TYPE_ID = "ae9715b4-2a72-4769-8121-e3894aec5b70";

  private static final boolean ACTIVE_BEFORE = false;
  private static final boolean ACTIVE_AFTER = true;

  @Override
  public void getAndVerifyResourceAfterCreate(Object resourceId) {
    verifyResource(getResource(resourceId), ACTIVE_BEFORE);
  }

  @Override
  public void getAndVerifyResourceAfterUpdate(Object resourceId) {
    verifyResource(getResource(resourceId), ACTIVE_AFTER);
  }

  @Override
  public String getResourceUrl() {
    return FACILITY_URL;
  }

  private void verifyResource(ValidatableResponse response, boolean active) {
    response
        .body("name", is(NAME))
        .body("description", is(DESCRIPTION))
        .body("code", is(CODE))
        .body("geographicZone.id", is(GEO_ZONE_ID))
        .body("type.id", is(TYPE_ID))
        .body("active", is(active))
        .body("enabled", is(active))
        .body("extraData", is(notNullValue()))
        .body("extraData.isManagedExternally", is("true"));
  }

}
