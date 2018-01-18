package org.openlmis.contract_tests.requisition;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.requisition.RequisitionStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.requisition.RequisitionStepDefs.BASE_URL_OF_REFERENCEDATA_SERVICE;

class ProcessingPeriodUtils {

  static String findPeriodByDate(JSONArray periods, LocalDate periodDate) {
    for (Object periodObj : periods) {
      JSONObject period = (JSONObject) periodObj;
      if (isWithinRange(periodDate, period)) {
        return (String) period.get("id");
      }
    }
    return null;
  }

  static boolean isWithinRange(LocalDate date, JSONObject period) {
    LocalDate startDate = parseDate(period.get("startDate"));
    LocalDate endDate = parseDate(period.get("endDate"));
    return !(date.isBefore(startDate) || date.isAfter(endDate));
  }

  static LocalDate parseDate(Object dateString) {
    return LocalDate.parse((String) dateString);
  }

  static JSONArray getExistingPeriods(String scheduleId) throws ParseException {
    ExtractableResponse<Response> periods = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("processingScheduleId", scheduleId)
        .when()
        .get(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/")
        .then()
        .statusCode(200)
        .extract();
    JSONParser parser = new JSONParser();
    return (JSONArray) ((JSONObject) parser.parse(periods.asString())).get("content");
  }

}
