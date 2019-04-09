package org.openlmis.contract_tests.hapifhir;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openlmis.contract_tests.common.JsonFieldHelper;

public abstract class BaseTestHelper implements TestHelper {

  private static final Map<String, ResourceFinder> FINDERS;

  static {
    FINDERS = new HashMap<>();
    FINDERS.put(GEO_ZONE, new GeographicZoneFinder());
    FINDERS.put(LOCATION, new LocationFinder());
  }

  private final Map<String, Response> savedResponses = new HashMap<>();
  private final Map<String, JSONObject> savedResources = new HashMap<>();
  private final Map<String, Map<String, List<String>>> transformations = new HashMap<>();

  @Override
  public Response createResource(String body) throws ParseException {
    return doRequest()
        .body(applyTransformations(body))
        .when()
        .post(getResourceUrl());
  }

  @Override
  public Response updateResource(Object resourceId, String body) throws ParseException {
    return doRequest()
        .body(applyTransformations(body))
        .when()
        .put(getResourceUrl() + "/" + resourceId);
  }

  @Override
  public void findAndSaveResource(String resource, Map<String, String> parameters) {
    savedResponses.put(resource, findFinder(resource).find(parameters));
  }

  @Override
  public void verifyFoundResource(String resource) throws ParseException {
    String results = savedResponses
        .get(resource)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .asString();

    Object json = new JSONParser().parse(results);
    JSONArray content;

    if (json instanceof JSONArray) {
      content = (JSONArray) json;
    } else {
      JSONObject object = (JSONObject) json;
      content = object.containsKey("content")
          ? (JSONArray) object.get("content")
          : (JSONArray) object.get("entry");
    }

    assertThat(content).hasSize(1);

    JSONObject first = (JSONObject) content.get(0);
    if (first.containsKey("resource")) {
      savedResources.put(resource, (JSONObject) first.get("resource"));
    } else {
      savedResources.put(resource, first);
    }
  }

  @Override
  public void saveTransformation(String resource, String field, String path) {
    transformations.computeIfAbsent(resource, key -> new HashMap<>());
    transformations.get(resource).computeIfAbsent(field, key -> new ArrayList<>());
    transformations.get(resource).get(field).add(path);
  }

  abstract String getResourceUrl();

  private ResourceFinder findFinder(String resource) {
    return Optional
        .ofNullable(FINDERS.get(resource))
        .orElseThrow(() -> new IllegalStateException("Unsupported resource: " + resource));
  }

  private String applyTransformations(String body) throws ParseException {
    if (transformations.isEmpty()) {
      return body;
    }

    JSONObject json = (JSONObject) new JSONParser().parse(body);

    for (Entry<String, Map<String, List<String>>> resourceEntry : transformations.entrySet()) {
      JSONObject resource = savedResources.get(resourceEntry.getKey());

      for (Entry<String, List<String>> fieldEntry : resourceEntry.getValue().entrySet()) {
        Object fieldValue = JsonFieldHelper.getField(resource, fieldEntry.getKey());

        for (String path : fieldEntry.getValue()) {
          Object newFieldValue = fieldValue;

          if (resource.containsKey("resourceType") && "partOf.reference".equalsIgnoreCase(path)) {
            newFieldValue = resource.get("resourceType") + "/" + fieldValue;
          }

          JsonFieldHelper.setField(json, path, newFieldValue);
        }
      }
    }

    return json.toString();
  }

  static RequestSpecification doRequest() {
    return given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-results=1");
  }

  private static final class GeographicZoneFinder implements ResourceFinder {

    @Override
    public Response find(Map<String, String> parameters) {
      JSONObject body = new JSONObject(parameters);

      return doRequest()
          .body(body)
          .when()
          .post(GEO_ZONE_URL + "/search");
    }

  }

  private static final class LocationFinder implements ResourceFinder {

    @Override
    public Response find(Map<String, String> parameters) {
      RequestSpecification specification = doRequest();
      parameters.forEach(specification::queryParam);

      return specification.when().get(LOCATION_URL);
    }

  }

  private interface ResourceFinder {

    Response find(Map<String, String> parameters);

  }

}
