package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;

public abstract class BaseTestHelper implements TestHelper {

  @Override
  public Response createResource(String body) {
    return given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .body(body)
        .when()
        .post(getResourceUrl());
  }

  @Override
  public Response updateResource(Object resourceId, String body) {
    return given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .body(body)
        .when()
        .put(getResourceUrl() + "/" + resourceId);
  }

  abstract String getResourceUrl();

}
