package org.openlmis.contract_tests.notification;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.LoginStepDefs.USER_REFERENCE_DATA_ID;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Collection;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openlmis.contract_tests.common.JsonFieldSetter;

public class VerificationStepDefs {

  private static final String TOKEN_PARAM = "token";
  private static final String ID_PARAM = "id";

  private static final String BASE_URL_OF_NOTIFICATION_SERVICE =
      baseUrlOfService("notification");

  private static final String USER_CONTACT_DETAILS_URL = BASE_URL_OF_NOTIFICATION_SERVICE
      + "userContactDetails/{id}";
  private static final String VERIFICATIONS_URL = USER_CONTACT_DETAILS_URL
      + "/verifications";
  private static final String VERIFICATION_TOKEN_URL = VERIFICATIONS_URL
      + "/{token}";

  private Response contactDetailsResponse;
  private Response verificationsResponse;
  private Response verificationTokenResponse;

  private String contactDetails;
  private String email;
  private String token;

  @When("^I try to get my contact details$")
  public void tryToGetContactDetails() {
    contactDetailsResponse = given()
        .pathParam(ID_PARAM, USER_REFERENCE_DATA_ID)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(USER_CONTACT_DETAILS_URL);
  }

  @Then("^I should get my contact details$")
  public void shouldGetContactDetails() {
    contactDetailsResponse
        .then()
        .statusCode(200);

    contactDetails = contactDetailsResponse.asString();
    email = from(contactDetails).get("emailDetails.email");
  }

  @Then("^I should get my contact details with:$")
  public void shouldGetContactDetailsWith(DataTable table) {
    shouldGetContactDetails();

    table
        .asMaps()
        .stream()
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .forEach(entry -> {
          String value = from(contactDetails).getString(entry.getKey());
          assertThat(value, is(entry.getValue()));
        });
  }

  @When("^I try to update my contact details with:$")
  public void updateContactDetails(DataTable table) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject body = (JSONObject) parser.parse(contactDetails);

    table
        .asMaps()
        .stream()
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .forEach(entry -> JsonFieldSetter.setField(body, entry.getKey(), entry.getValue()));

    contactDetailsResponse = given()
        .pathParam(ID_PARAM, USER_REFERENCE_DATA_ID)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .when()
        .body(body.toJSONString())
        .put(USER_CONTACT_DETAILS_URL);
  }

  @Then("^I should get update response with old email address$")
  public void shouldGetUpdateResponseWithOldEmailAddress() {
    contactDetailsResponse
        .then()
        .statusCode(200)
        .body("emailDetails.email", is(email));
  }

  @When("^I try to get verification token$")
  public void tryToGetVerificationToken() {
    verificationsResponse = given()
        .pathParam(ID_PARAM, USER_REFERENCE_DATA_ID)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(VERIFICATIONS_URL);
  }

  @Then("^I should get verification response for (.+)$")
  public void shouldGetVerificationResponseFor(String email) {
    verificationsResponse
        .then()
        .statusCode(200)
        .body("emailAddress", is(email));
    token = from(verificationsResponse.asString()).getString("token");
  }

  @When("^I try to verify my new email address$")
  public void tryToVerifyEmailAddress() {
    verificationTokenResponse = given()
        .pathParam(ID_PARAM, USER_REFERENCE_DATA_ID)
        .pathParam(TOKEN_PARAM, token)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(VERIFICATION_TOKEN_URL);
  }

  @Then("^I should get success verification response$")
  public void shouldGetSuccessVerificationResponse() {
    verificationTokenResponse
        .then()
        .statusCode(200);
  }

}
