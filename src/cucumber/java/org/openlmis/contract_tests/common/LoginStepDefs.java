package org.openlmis.contract_tests.common;

import cucumber.api.java.en.Given;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;
import static org.openlmis.contract_tests.common.TestVariableReader.passwordOf;

public class LoginStepDefs {
    public static String ACCESS_TOKEN;

    @Given("^I have logged in as (.*)$")
    public void IHaveLoggedInAs(String userName) throws Throwable {

        String tokenResponseString = given()
                .header("Authorization", "Basic dHJ1c3RlZC1jbGllbnQ6c2VjcmV0")
                .param("grant_type", "password")
                .param("username", userName)
                .param("password", passwordOf(userName))
                .when()
                .post(baseUrlOfService("auth") + "token")
                .asString();

        ACCESS_TOKEN = from(tokenResponseString).get("access_token");
    }
}
