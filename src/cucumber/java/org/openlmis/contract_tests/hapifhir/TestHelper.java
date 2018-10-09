package org.openlmis.contract_tests.hapifhir;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public interface TestHelper {

  Response createResource(String bodyAsString);

  void verifyLocationAfterCreate(ValidatableResponse response);

  Response updateResource(Object resourceId, String bodyAsString);

  void verifyLocationAfterUpdate(ValidatableResponse response);

}
