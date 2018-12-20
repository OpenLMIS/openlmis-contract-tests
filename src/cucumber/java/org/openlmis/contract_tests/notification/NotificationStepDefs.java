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

package org.openlmis.contract_tests.notification;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.regex.Pattern;
import org.assertj.core.util.Sets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NotificationStepDefs {

  private static final String BASE_URL_OF_NOTIFICATION_SERVICE = baseUrlOfService("notification");

  private Set<String> notifications = Sets.newHashSet();
  private String userId;


  @When("^I try to find notifications for user (.+) from last (\\d+) days$")
  public void tryToFindNotificationsForUser(String userId, Integer days) throws ParseException {
    this.userId = userId;

    ZonedDateTime to = ZonedDateTime.now();
    ZonedDateTime from = to.minusDays(days);

    String pageAsString = given()
        .when()
        .contentType(ContentType.JSON)
        .queryParam("userId", userId)
        .queryParam("sendingDateFrom", from.toString())
        .queryParam("sendingDateTo", to.toString())
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .get(BASE_URL_OF_NOTIFICATION_SERVICE + "notifications")
        .then()
        .statusCode(200)
        .extract()
        .asString();

    JSONParser parser = new JSONParser();
    JSONObject page = (JSONObject) parser.parse(pageAsString);
    JSONArray content = (JSONArray) page.get("content");

    for (Object item : content) {
      JSONObject json = (JSONObject) item;
      JSONObject messages = (JSONObject) json.get("messages");
      JSONObject message = (JSONObject) messages.get("email");
      String body = (String) message.get("body");

      notifications.add(body);
    }

    System.out.println(notifications);
  }

  @Then("^I should get a notification that match the following regex$")
  public void shouldGetNotificationWithBody(String regex) {
    Pattern pattern = Pattern.compile(regex);

    assertThat(notifications)
        .as("No notifications for user %s", userId)
        .isNotEmpty();

    assertThat(notifications)
        .as("Can't find a notification with the given body")
        .anyMatch(body -> pattern.matcher(body).find());
  }

}
