package org.openlmis.contract_tests.hapifhir;

import com.github.tomakehurst.wiremock.client.WireMock;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

public class SubscriptionTestHelper {

  private static final String BASE_HAPI_FHIR_URL = baseUrlOfService("hapifhir");
  private static final String SUBSCRIPTION_URI = BASE_HAPI_FHIR_URL + "Subscription";
  public static final String LOCATION_URI = "Location/a5d519aa-1ab4-49e8-b720-07cc647108ea";

  private WireMock wireMock = new WireMock("wiremock", 8080);

  public void stubForLocation() {
    WireMock.configureFor("wiremock", 8080);
    wireMock.stubFor(
        put(
            urlPathMatching("/fhir_locations/Location/*"))
            .withHeader("Authentication", equalTo("Bearer 04199b94-15ce-4405-969c-05dedf4c073c"))
            .willReturn(aResponse().withStatus(200))
    );
  }

  public void createAFHIRSubscriptionResource(String body) throws ParseException {
    JSONObject json = (JSONObject) new JSONParser().parse(body);
    ValidatableResponse response = given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .body(json)
        .when()
        .post(SUBSCRIPTION_URI)
        .then()
        .statusCode(HttpStatus.SC_CREATED);

    response.body("resourceType", is("OperationOutcome"))
        .body("issue[0].diagnostics", containsString("Successfully created resource"));

  }

  public void verifyThatStubWasCalled() {
    wireMock.verify(
        putRequestedFor(
            urlPathMatching("/fhir_locations/" + LOCATION_URI))
            .withRequestBody(
                matchingJsonPath("$..name", containing("Contract Test S  FHIR Geographic Zone 2")))
            .withRequestBody(matchingJsonPath("$.resourceType", containing("Location")))
    );

  }

  public void createFHIRLocationResource(String body) throws ParseException {
    JSONObject json = (JSONObject) new JSONParser().parse(body);
    ValidatableResponse response = given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "bearer " + ACCESS_TOKEN)
        .body(json)
        .when()
        .put(BASE_HAPI_FHIR_URL + LOCATION_URI)
        .then()
        .statusCode(HttpStatus.SC_CREATED);

    response.body("resourceType", is("OperationOutcome"))
        .body("issue[0].diagnostics",
            containsString("Successfully created resource \"" + LOCATION_URI + "/_history/1\""));
  }
}
