package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

class GeographicZoneTestHelper extends OlmisResourceTestHelper {

  private static final String PHYSICAL_TYPE = "area";

  private static final String CODE_1 = "CT-GZ-1";
  private static final String CODE_2 = "CT-GZ-2";

  private static final String NAME_1 = "Contract Test Geographic Zone 1";
  private static final String NAME_2 = "Contract Test Geographic Zone 2";

  private static final String LEVEL_ID_1 = "6b78e6c6-292e-4733-bb9c-3d802ad61206";
  private static final String LEVEL_ID_2 = "9b497d87-cdd9-400e-bb04-fae0bf6a9491";

  private static final int LATITUDE_BEFORE = 0;
  private static final int LATITUDE_AFTER = 2;

  private static final int LONGITUDE_BEFORE = 0;
  private static final int LONGITUDE_AFTER = 2;

  @Override
  String getResourceUrl() {
    return GEO_ZONE_URL;
  }

  @Override
  public void verifyFhirResourceAfterCreate(String resource, Object resourceId) {
    ValidatableResponse response = getLocation(resourceId, "1");
    verifyLocation(response, LATITUDE_BEFORE, LONGITUDE_BEFORE);
  }

  @Override
  public void verifyFhirResourceAfterUpdate(String resource, Object resourceId) {
    ValidatableResponse response = getLocation(resourceId, "2");
    verifyLocation(response, LATITUDE_AFTER, LONGITUDE_AFTER);
  }

  private void verifyLocation(ValidatableResponse response, int latitude, int longitude) {
    String resource = response
        .body("position.latitude", is(latitude))
        .body("position.longitude", is(longitude))
        .body("physicalType.coding.code", hasItem(PHYSICAL_TYPE))
        .extract()
        .asString();

    JsonPath from = from(resource);

    String reference = from.getString("entry[0].resource.partOf.reference");
    List<String> aliases = from.getList("entry[0].resource.alias");
    List<String> identifierValues = from.getList("entry[0].resource.identifier.value");
    String name = from.getString("entry[0].resource.name");

    if (null == reference) {
      assertThat(aliases).contains(CODE_1);
      assertThat(name).isEqualTo(NAME_1);
      assertThat(identifierValues).contains(GEO_LEVEL_URL + "/" + LEVEL_ID_1);
    } else {
      assertThat(aliases).contains(CODE_2);
      assertThat(name).isEqualTo(NAME_2);
      assertThat(identifierValues).contains(GEO_LEVEL_URL + "/" + LEVEL_ID_2);

      given()
          .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
          .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1")
          .when()
          .get(BASE_HAPI_FHIR_URL + reference)
          .then()
          .statusCode(HttpStatus.SC_OK)
          .body("alias", hasItem(CODE_1))
          .body("name", is(NAME_1));
    }
  }

}
