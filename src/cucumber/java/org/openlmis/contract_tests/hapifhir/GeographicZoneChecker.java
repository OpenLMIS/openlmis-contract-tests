package org.openlmis.contract_tests.hapifhir;

import static io.restassured.path.json.JsonPath.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import java.util.Map;

class GeographicZoneChecker implements ResourceChecker {

  private static final String BASE_REFERENCE_DATA_URL = baseUrlOfService("referencedata");
  private static final String GEO_ZONE_URL = BASE_REFERENCE_DATA_URL + "geographicZones";

  private static final String NAME_1 = "Contract Test FHIR Geographic Zone 1";
  private static final String NAME_2 = "Contract Test FHIR Geographic Zone 2";

  private static final String CODE_1 = "CT-FHIR-GZ-1";
  private static final String CODE_2 = "CT-FHIR-GZ-2";


  private static final String COUNTRY_LEVEL_ID = "6b78e6c6-292e-4733-bb9c-3d802ad61206";
  private static final String REGION_LEVEL_ID = "9b497d87-cdd9-400e-bb04-fae0bf6a9491";
  private static final String PARENT_ID = "4e471242-da63-436c-8157-ade3e615c848";

  private static final float LATITUDE_BEFORE = 0f;
  private static final float LATITUDE_AFTER = 2f;

  private static final float LONGITUDE_BEFORE = 0f;
  private static final float LONGITUDE_AFTER = 2f;

  @Override
  public void getAndVerifyResourceAfterCreate(Object resourceId) {
    verifyResource(getResource(resourceId), LATITUDE_BEFORE, LONGITUDE_BEFORE);
  }

  @Override
  public void getAndVerifyResourceAfterUpdate(Object resourceId) {
    verifyResource(getResource(resourceId), LATITUDE_AFTER, LONGITUDE_AFTER);
  }

  @Override
  public String getResourceUrl() {
    return GEO_ZONE_URL;
  }

  private void verifyResource(ValidatableResponse response, float latitude, float longitute) {
    String resource = response
        .body("latitude", is(latitude))
        .body("longitude", is(longitute))
        .body("extraData", is(notNullValue()))
        .body("extraData.isManagedExternally", is("true"))
        .extract()
        .asString();

    JsonPath from = from(resource);

    Map level = from.get("level");
    Map parent = from.get("parent");

    if (null == parent) {
      assertThat(from.getString("name")).isEqualTo(NAME_1);
      assertThat(from.getString("code")).isEqualTo(CODE_1);
      assertThat(level.get("id")).isEqualTo(COUNTRY_LEVEL_ID);
    } else {
      assertThat(from.getString("name")).isEqualTo(NAME_2);
      assertThat(from.getString("code")).isEqualTo(CODE_2);
      assertThat(level.get("id")).isEqualTo(REGION_LEVEL_ID);
      assertThat(parent.get("id")).isEqualTo(PARENT_ID);
    }
  }

}
