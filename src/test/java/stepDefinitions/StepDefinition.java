package stepDefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import logs.LogsManager;
import resources.APIResources;
import resources.Utils;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class StepDefinition extends Utils {

    RequestSpecification req; // To hold the request specification for each scenario
    Response response;

    // Static variables to hold the single active entity of each type
    public static String boardId;
    public static String listId;
    public static String cardId;
    public static String checklistId;

    public static String last_boardId;
    public static String last_listId;
    public static String last_cardId;
    public static String last_checklistId;


    @When("I create a {string} with name {string}")
    public void i_create_a_with_name(String creationType, String creationName) throws IOException {
        APIResources apiResources = APIResources.fromString(creationType);

        RequestSpecification reqSpec = given().spec(requestSpecification())
                .queryParam("name", creationName);

        // Add parent ID if this entity needs one
        String parentParam = getParentParamName(creationType);
        if (parentParam != null) {
            reqSpec.queryParam(parentParam, getParentId(creationType));
        }
        response = reqSpec.when().post(apiResources.getResource());

        String newId = getJsonPath(response, "id");
        setIdForType(creationType, newId);
        LogsManager.info("Successfully created " + creationType + ". ID: " + newId);
    }

    @Then("the status code should be {int}")
    public void the_status_code_should_be(int expectedCode) {
        int actualCode = response.getStatusCode();
        LogsManager.debug("Validating Status Code. Expected:", String.valueOf(expectedCode), "Actual:", String.valueOf(actualCode));
        assertEquals(expectedCode, actualCode);
    }

    @Then("the response contains a non-empty {string}")
    public void the_response_contains_a_non_empty(String key) {
        String value = getJsonPath(response, key);
        LogsManager.debug("Checking if field", key, "is not empty. Found value:", value);
        assertNotNull("Field " + key + " was null!", value);
        assertFalse("Field " + key + " was empty!", value.isEmpty());

    }

    @Then("the response name should be {string}")
    public void the_response_name_should_be(String name) {
        String actualName = getJsonPath(response, "name");
        LogsManager.info("Verifying name. Expected: " + name + " | Actual: " + actualName);
        assertEquals(actualName, name);
    }

    @When("I get the {string} by id")
    public void i_get_the_by_id(String getType) throws IOException {
        APIResources apiResources = APIResources.fromString(getType);
        LogsManager.info("Getting board by ID: " + getIdForType(getType));
        response = given().spec(requestSpecification())
                .pathParam("id", getIdForType(getType))
                .when()
                .get(apiResources.getResourceWithId());
    }

    @Given("a {string} exists with name {string}")
    public void aExistsWithName(String creationType, String creationName) throws IOException {
        if (getIdForType(creationType) != null) {
            i_get_the_by_id(creationType);
            if (getJsonPath(response, "name").equals(creationName)) {
                LogsManager.info("Board with name '" + creationName + "' already exists with ID: " + getIdForType(creationType));
            } else {
                LogsManager.warn("Existing " +
                        getIdForType(creationType) + " name '" + getJsonPath(response, "name") + "' does not match expected name '" + creationName +
                        "'. Creating new " + getIdForType(creationType) + "...");
                i_create_a_with_name(creationType, creationName);
            }
        } else {
            LogsManager.warn(getIdForType(creationType) + " ID was null, creating new board...");
            i_create_a_with_name(creationType, creationName);
        }

    }

    @When("I update the {string} name to {string}")
    public void iUpdateTheNameTo(String updateType, String updateName) throws IOException {
        APIResources apiResources = APIResources.fromString(updateType);
        LogsManager.info("Updating board " + getIdForType(updateType) + " to name: " + updateName);
        response = given().spec(requestSpecification()).pathParam("id", getIdForType(updateType))
                .queryParam("name", updateName)
                .when()
                .put(apiResources.getResourceWithId());
        setIdForType(updateType, getJsonPath(response, "id"));
    }

    @When("I delete the {string}")
    public void iDeleteThe(String deleteType) throws IOException {
        APIResources apiResources = APIResources.fromString(deleteType);
        if (deleteType.equalsIgnoreCase("board")) {
            LogsManager.warn("Deleting board with ID: " + boardId);
            response = given().spec(requestSpecification()).pathParam("id", boardId)
                    .when()
                    .delete(apiResources.getResourceWithId());
            last_boardId = boardId;
            boardId = null; // Clear the boardId after deletion
            listId = null;
            cardId = null;
            checklistId = null;
        } else if (deleteType.equalsIgnoreCase("list")) {
            LogsManager.warn("Deleting list with ID: " + listId);
            response = given().spec(requestSpecification()).pathParam("id", listId)
                    .queryParam("value", "true")
                    .when()
                    .put(apiResources.getResourceArchive());
            last_listId = listId;
            listId = null; // Clear the listId after deletion
            cardId = null;
            checklistId = null;
        } else if (deleteType.equalsIgnoreCase("card")) {
            LogsManager.warn("Deleting card with ID: " + cardId);
            response = given().spec(requestSpecification()).pathParam("id", cardId)
                    .when()
                    .delete(apiResources.getResourceWithId());
            last_cardId = cardId;
            cardId = null; // Clear the cardId after deletion
            checklistId = null;
        } else if (deleteType.equalsIgnoreCase("checklist")) {
            LogsManager.warn("Deleting checklist with ID: " + checklistId);
            response = given().spec(requestSpecification()).pathParam("id", checklistId)
                    .when()
                    .delete(apiResources.getResourceWithId());
            last_checklistId = checklistId;
            checklistId = null; // Clear the cardId after deletion
        }

    }


    @When("I create a {string} with an empty payload")
    public void iCreateAWithAnEmptyPayload(String creationType) throws IOException {
        LogsManager.info("Attempting to create " + creationType + " with empty payload");
        APIResources apiResources = APIResources.fromString(creationType);
        response = given().spec(requestSpecification())
                .when()
                .post(apiResources.getResource());
    }

    @When("I get the deleted {string} by id")
    public void iGetTheDeletedById(String getType) throws IOException {
        APIResources apiResources = APIResources.fromString(getType);
        LogsManager.info("Getting " + getType + " by ID: " + last_listId);
        response = given().spec(requestSpecification()).
                pathParam("id", getLastIdForType(getType))
                .when()
                .get(apiResources.getResourceWithId());

    }


    private String getIdForType(String type) {
        switch (type.toLowerCase()) {
            case "board":
                return boardId;
            case "list":
                return listId;
            case "card":
                return cardId;
            case "checklist":
                return checklistId;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }


    private String getLastIdForType(String type) {
        switch (type.toLowerCase()) {
            case "board":
                return last_boardId;
            case "list":
                return last_listId;
            case "card":
                return last_cardId;
            case "checklist":
                return last_checklistId;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private void setIdForType(String type, String id) {
        switch (type.toLowerCase()) {
            case "board":
                boardId = id;
                break;
            case "list":
                listId = id;
                break;
            case "card":
                cardId = id;
                break;
            case "checklist":
                checklistId = id;
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private String getParentParamName(String type) {
        switch (type.toLowerCase()) {
            case "list":
                return "idBoard";
            case "card":
                return "idList";
            case "checklist":
                return "idCard";
            default:
                return null; // boards have no parent
        }
    }

    private String getParentId(String type) {
        switch (type.toLowerCase()) {
            case "list":
                return boardId;
            case "card":
                return listId;
            case "checklist":
                return cardId;
            default:
                return null;
        }
    }

    @Given("Delete All boards")
    public void deleteAllBoards() throws IOException {
        response = given().spec(requestSpecification())
                .when()
                .get("/members/me/boards");
        List<String> boardIds = response.jsonPath().getList("id");
        LogsManager.info("Found " + boardIds.size() + " boards to delete.");
        for (String id : boardIds) {
            LogsManager.warn("Deleting board with ID: " + id);
            given().spec(requestSpecification())
                    .pathParam("id", id)
                    .when()
                    .delete("/boards/{id}");
        }
    }
}
