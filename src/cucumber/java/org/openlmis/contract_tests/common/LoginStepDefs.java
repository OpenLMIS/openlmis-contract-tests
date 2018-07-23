/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.contract_tests.common;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;
import static org.openlmis.contract_tests.common.TestVariableReader.passwordOf;

import cucumber.api.java.en.Given;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;

public class LoginStepDefs {

  public static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
  public static String ACCESS_TOKEN;
  public static UUID USER_REFERENCE_DATA_ID;

  @Given("^I have logged in as (.*)$")
  public void haveLoggedInAs(String userName) throws Throwable {
    String plainCreds = isNotBlank(userName)
        ? "user-client:changeme"
        : "trusted-client:secret";
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    String tokenResponseString = given()
        .header("Authorization", "Basic " + base64Creds)
        .param("grant_type", "password")
        .param("username", userName)
        .param("password", passwordOf(userName))
        .when()
        .post(baseUrlOfService("oauth") + "token")
        .asString();

    ACCESS_TOKEN = from(tokenResponseString).get("access_token");
    USER_REFERENCE_DATA_ID = from(tokenResponseString).getUUID("referenceDataUserId");

    assertThat(
        "Can't log as >" + userName + "< with password >" + passwordOf(userName) + "<",
        ACCESS_TOKEN,
        is(notNullValue())
    );
  }

  @Given("^I logout$")
  public void tryLogout() {
    given()
        .queryParam("access_token", ACCESS_TOKEN)
        .when()
        .post(baseUrlOfService("oauth") + "/users/logout");
  }

  @Given("^I am not logged$")
  public void notLogged() {
    if (null != ACCESS_TOKEN) {
      tryLogout();
    }
  }

  @Given("^I pause 5 seconds for right assignment regeneration$")
  public void pauseFiveSeconds() throws InterruptedException {
    Thread.sleep(5000);
  }
}
