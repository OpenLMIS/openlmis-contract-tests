package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.http.HttpHeaders;

public class LocationTestHelper extends FhirResourceTestHelper {

  private static final Map<String, ResourceChecker> CHECKERS;

  static {
    CHECKERS = new HashMap<>();
    CHECKERS.put(FACILITY, new FacilityChecker());
    CHECKERS.put(GEO_ZONE, new GeographicZoneChecker());
  }

  @Override
  String getResourceUrl() {
    return LOCATION_URL;
  }

  @Override
  public void verifyFhirResourceAfterCreate(String resource, Object resourceId) {
    findChecker(resource).getAndVerifyResourceAfterCreate(resourceId);
  }

  @Override
  public void verifyFhirResourceAfterUpdate(String resource, Object resourceId) {
    findChecker(resource).getAndVerifyResourceAfterUpdate(resourceId);
  }

  private ResourceChecker findChecker(String resource) {
    return Optional
        .ofNullable(CHECKERS.get(resource))
        .orElseThrow(() -> new IllegalStateException("Unsupported resource: " + resource));
  }

  private static final class FacilityChecker implements ResourceChecker {

    private static final String CODE = "CT-FHIR-F";
    private static final String NAME = "Contract Test FHIR Facility";
    private static final String DESCRIPTION =
        "This is an example location which should be added to the reference data service";

    private static final String GEO_ZONE_CODE = "CT-FHIR-GZ-2";
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
          .body("geographicZone.code", is(GEO_ZONE_CODE))
          .body("type.id", is(TYPE_ID))
          .body("active", is(active))
          .body("enabled", is(active))
          .body("extraData", is(notNullValue()))
          .body("extraData.isManagedExternally", is("true"));
    }

  }

  private static final class GeographicZoneChecker implements ResourceChecker {

    private static final String NAME_1 = "Contract Test FHIR Geographic Zone 1";
    private static final String NAME_2 = "Contract Test FHIR Geographic Zone 2";

    private static final String CODE_1 = "CT-FHIR-GZ-1";
    private static final String CODE_2 = "CT-FHIR-GZ-2";

    private static final String COUNTRY_LEVEL_ID = "6b78e6c6-292e-4733-bb9c-3d802ad61206";
    private static final String REGION_LEVEL_ID = "9b497d87-cdd9-400e-bb04-fae0bf6a9491";
    private static final String PARENT_CODE = "CT-FHIR-GZ-1";

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
        assertThat(parent.get("code")).isEqualTo(PARENT_CODE);
      }
    }

  }

  private interface ResourceChecker {

    void getAndVerifyResourceAfterCreate(Object resourceId);

    void getAndVerifyResourceAfterUpdate(Object resourceId);

    String getResourceUrl();

    default ValidatableResponse getResource(Object resourceId) {
      return given()
          .contentType(ContentType.JSON)
          .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
          .when()
          .get(getResourceUrl() + "/" + resourceId)
          .then();
    }

  }
}
