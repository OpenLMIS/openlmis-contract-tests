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
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.assertj.core.util.Lists;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NotificationStepDefs {

  private static final String BASE_URL_OF_NOTIFICATION_SERVICE = baseUrlOfService("notification");

  private List<JSONObject> notifications = Lists.newArrayList();
  private String userId;


  @When("^I try to find notifications for user (.+)$")
  public void tryToFindNotificationsForUser(String userId) throws ParseException {
    this.userId = userId;

    JSONParser parser = new JSONParser();
    int pageNumber = 0;
    int pageSize = 10_000;

    while (true) {
      String pageAsString = given()
          .when()
          .contentType(ContentType.JSON)
          .queryParam("page", pageNumber)
          .queryParam("size", pageSize)
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .get(BASE_URL_OF_NOTIFICATION_SERVICE + "notifications")
          .then()
          .statusCode(200)
          .extract()
          .asString();
      JSONObject page = (JSONObject) parser.parse(pageAsString);
      JSONArray content = (JSONArray) page.get("content");

      if (content.isEmpty()) {
        break;
      }

      for (Object item : content) {
        JSONObject obj = (JSONObject) item;
        if (equalsIgnoreCase(userId, obj.get("userId").toString())) {
          notifications.add(obj);
        }
      }

      pageNumber += 1;
    }
  }

  @Then("^I should get a notification that match the following regex$")
  public void shouldGetNotificationWithBody(String regex) {
    assertThat(notifications)
        .as("No notifications for user %s", userId)
        .isNotEmpty();

    Set<String> bodies = notifications
        .stream()
        .map(json -> (JSONObject) json.get("messages"))
        .filter(Objects::nonNull)
        .map(messages -> (JSONObject) messages.get("email"))
        .filter(Objects::nonNull)
        .map(message -> (String) message.get("body"))
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    assertThat(bodies)
        .as("Can't find a notification with the given body")
        .anyMatch(body -> Pattern.matches(regex, body));
  }

}
