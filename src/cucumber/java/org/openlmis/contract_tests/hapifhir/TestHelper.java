package org.openlmis.contract_tests.hapifhir;

import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

interface TestHelper {
  String BASE_HAPI_FHIR_URL = baseUrlOfService("hapifhir");
  String LOCATION_URL = BASE_HAPI_FHIR_URL + "Location";

  String BASE_REFERENCE_DATA_URL = baseUrlOfService("referencedata");

  String GEO_ZONE_URL = BASE_REFERENCE_DATA_URL + "geographicZones";
  String GEO_LEVEL_URL = BASE_REFERENCE_DATA_URL + "geographicLevels";
  String FACILITY_URL = BASE_REFERENCE_DATA_URL + "facilities";

  String LOCATION = "location";
  String FACILITY = "facility";
  String GEO_ZONE = "geographic zone";

  Response createResource(String body) throws ParseException;

  Response updateResource(Object resourceId, String body) throws ParseException;

  JSONObject getResource(ValidatableResponse response) throws ParseException;

  void verifyFhirResourceAfterCreate(String resource, Object resourceId);

  void verifyFhirResourceAfterUpdate(String resource, Object resourceId);

  void findAndSaveResource(String resource, Map<String, String> parameters);

  void verifyFoundResource(String resource) throws ParseException;

  void saveTransformation(String resource, String field, String path);

}
