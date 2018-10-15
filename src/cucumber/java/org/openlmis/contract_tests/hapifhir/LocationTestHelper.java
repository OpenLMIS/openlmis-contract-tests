package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LocationTestHelper extends BaseTestHelper {

  private static final String BASE_HAPI_FHIR_URL = baseUrlOfService("hapifhir");
  private static final String LOCATION_URL = BASE_HAPI_FHIR_URL + "Location";

  private static final String FACILITY = "facility";
  private static final String GEO_ZONE = "geographic zone";

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
  public JSONObject getResource(ValidatableResponse response) throws Exception {
    String locationHref = response
        .extract()
        .header(HttpHeaders.CONTENT_LOCATION);

    TimeUnit.SECONDS.sleep(1);

    String locationAsString = given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .when()
        .get(locationHref)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .asString();

    return (JSONObject) new JSONParser().parse(locationAsString);
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

}
