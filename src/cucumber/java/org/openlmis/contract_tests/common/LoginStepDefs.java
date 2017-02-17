package org.openlmis.contract_tests.common;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;
import static org.openlmis.contract_tests.common.TestVariableReader.passwordOf;

import cucumber.api.java.en.Given;
import org.apache.commons.codec.binary.Base64;

public class LoginStepDefs {

  public static String ACCESS_TOKEN;

  @Given("^I have logged in as (.*)$")
  public void haveLoggedInAs(String userName) throws Throwable {

    String plainCreds = "trusted-client:secret";
    if (userName.equals("administrator") || userName.contains("srmanager")) {
      plainCreds = "user-client:changeme";
    }

    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    String tokenResponseString = given()
        .header("Authorization", "Basic " + base64Creds)
        .param("grant_type", "password")
        .param("username", userName)
        .param("password", passwordOf(userName))
        .when()
        .post(baseUrlOfService("auth") + "token")
        .asString();

    ACCESS_TOKEN = from(tokenResponseString).get("access_token");
  }

  @Given("^I logout$")
  public void tryLogout() {
    given()
        .queryParam("access_token", ACCESS_TOKEN)
        .when()
        .post(baseUrlOfService("auth") + "/users/logout");
  }
}
