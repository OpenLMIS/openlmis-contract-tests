package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;

interface ResourceChecker {

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
