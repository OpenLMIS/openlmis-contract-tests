package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;
import static org.openlmis.contract_tests.requisition.RequisitionStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.stockmanagement.StockCardsStepDefs.URL_OF_STOCK_CARD_SUMMARIES;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import java.util.List;

public class PhysicalInventoriesStepDefs {
    private static final String URL_PHYSICAL_INVENTORIES_SUBMIT =
            baseUrlOfService("stockmanagement") + "stockEvents/";

    private static final String URL_PHYSICAL_INVENTORIES =
            baseUrlOfService("stockmanagement") + "physicalInventories/";

    private static final String FACILITY_PARAM_NAME = "facility";
    private static final String PROGRAM_PARAM_NAME = "program";
    private static final String IS_DRAFT_PARAM_NAME = "isDraft";

    private Response response;
    private int quantity;
    private String physicalInventoryid;

    @When("^I try to get a draft with facilityId: (.*), programId: (.*)$")
    public void iTryToGetADraftWithFacilityIdAndProgramId(String facilityId, String programId)
            throws Throwable {
        response = given().contentType(ContentType.JSON)
                .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .queryParam(FACILITY_PARAM_NAME, facilityId)
                .queryParam(PROGRAM_PARAM_NAME, programId)
                .queryParam(IS_DRAFT_PARAM_NAME, true)
                .when()
                .get(URL_PHYSICAL_INVENTORIES);
    }

    @Then("^I should get OK with empty list$")
    public void iShouldGetNoContent() throws Throwable {
        List draft = response.then()
            .statusCode(SC_OK)
            .extract().as(List.class);
        assertEquals(0, draft.size());
    }

    @When("^I try to create new draft physical inventory$")
    public void iTryToCreateDraftPhysicalInventory(String bodyString) throws Throwable {
        response = given().contentType(ContentType.JSON)
                .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .body(bodyString)
                .when()
                .post(URL_PHYSICAL_INVENTORIES);
    }

    @Then("^I should get response of draft physical inventory created$")
    public void iShouldGetResponseOfDraftPhysicalInventoryCreated() throws Throwable {
        response.then()
                .statusCode(SC_CREATED);

        physicalInventoryid = from(response.asString()).get("id");
    }

    @When("^I try to save the draft physical inventory$")
    public void iTryToSaveTheDraftPhysicalInventory(String bodyString) throws Throwable {
        JSONObject jsonObject = new JSONObject(bodyString);
        jsonObject.put("id", physicalInventoryid);
        response = given().contentType(ContentType.JSON)
                .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .body(jsonObject.toString())
                .when()
                .put(URL_PHYSICAL_INVENTORIES + physicalInventoryid);
        quantity = JsonPath.from(bodyString).get("lineItems[0].quantity");
    }

    @Then("^I should get response of draft physical inventory saved$")
    public void iShouldGetResponseOfDraftPhysicalInventorySaved() throws Throwable {
        response.then()
                .statusCode(SC_OK);
    }

    @When("^I try to submit a physical inventory$")
    public void iTryToSubmitAPhysicalInventory(String bodyString) throws Throwable {
        JSONObject jsonObject = new JSONObject(bodyString);
        jsonObject.put("resourceId", physicalInventoryid);

        response = given().contentType(ContentType.JSON)
                .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .body(jsonObject.toString())
                .when()
                .post(URL_PHYSICAL_INVENTORIES_SUBMIT);
    }

    @Then("^I should get response of physical inventory submitted$")
    public void iShouldGetResponseOfPhysicalInventorySubmitted() throws Throwable {
        response.then().statusCode(SC_CREATED);
    }

    @When("^I try to get stock cards summaries by facilityId: (.*), programId: (.*)$")
    public void iTryToGetStockCardsSummariesByFacilityIdProgramId(String facilityId, String programId)
            throws Throwable {
        response = given().contentType(ContentType.JSON)
                .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .queryParam(FACILITY_PARAM_NAME, facilityId)
                .queryParam(PROGRAM_PARAM_NAME, programId)
                .when()
                .get(URL_OF_STOCK_CARD_SUMMARIES);
    }

    @Then("^I should get response of all stock cards summaries$")
    public void iShouldGetResponseOfAllStockCardsSummaries()
            throws Throwable {
        response.then()
                .statusCode(SC_OK);
    }

    @When("^I try to get physical inventory by id$")
    public void iTryToGetPhysicalInventoryById()
            throws Throwable {
        response = given()
            .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
            .when()
            .get(URL_PHYSICAL_INVENTORIES + physicalInventoryid);
    }

    @Then("^I should get response of submitted physical inventory")
    public void iShouldGetResponseOfSubmittedPhysicalInventory()
            throws Throwable {
        response.then()
            .statusCode(SC_OK)
            .body("isDraft", equalTo(false));
    }

    @Then("I should get response of saved draft physical inventory")
    public void iShouldGetResponseOfDraftPhysicalInventory()
            throws Throwable {
        response.then()
            .statusCode(SC_OK)
            .body("isDraft", equalTo(true));
    }

}
