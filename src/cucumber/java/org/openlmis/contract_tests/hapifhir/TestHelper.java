package org.openlmis.contract_tests.hapifhir;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

interface TestHelper {

  Response createResource(String body);

  Response updateResource(Object resourceId, String body);

  JSONObject getResource(ValidatableResponse response) throws Exception;

  void verifyFhirResourceAfterCreate(String resource, Object resourceId);

  void verifyFhirResourceAfterUpdate(String resource, Object resourceId);
}
