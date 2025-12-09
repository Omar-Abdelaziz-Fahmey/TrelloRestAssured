package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import resources.APIResources;
import resources.Utils;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class StepDefinition extends Utils {

    RequestSpecification reqSpec;
    Response response;
    String boardId;
    public static HashMap<String, String> boardMap = new HashMap<>();
    String selectedBoardId;
    public static HashMap<String, String> listMap = new HashMap<>();
    String selectedListId;
    public static HashMap<String, String> labelMap = new HashMap<>();
    String selectedLabelId;
    public static HashMap<String, String> cardMap = new HashMap<>();
    String selectedCardId;
    public static HashMap<String, String> checklistMap = new HashMap<>();
    String selectedChecklistId;
    public static HashMap<String, String> checkItemMap = new HashMap<>();
    String selectedCheckItemId;

    @Given("Create Board payload with {string}")
    public void create_board_payload_with(String boardName) throws IOException {
        reqSpec = given()
                .spec(requestSpecification())
                .queryParam("name", boardName);
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String httpMethod) {
        APIResources resourceAPI = APIResources.valueOf(resource);

        if (httpMethod.equalsIgnoreCase("Post")) {
            response = reqSpec
                    .when()
                    .post(resourceAPI.getResource());
        } else if (httpMethod.equalsIgnoreCase("Get")) {
            response = reqSpec
                    .when()
                    .get(resourceAPI.getResource());
        } else if (httpMethod.equalsIgnoreCase("Delete")) {
            response = reqSpec
                    .when()
                    .delete(resourceAPI.getResource());
        } else if (httpMethod.equalsIgnoreCase("Put")) {
            response = reqSpec
                    .when()
                    .put(resourceAPI.getResource());
        }


    }

    @Then("the API call is success with status code {int}")
    public void the_api_call_is_success_with_status_code(int int1) {
        assertEquals(int1, response.getStatusCode());
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(String key, String value) {
        assertEquals(value, getJsonPath(response, key));
    }


    @Then("store {string} from response as {string}")
    public void store_from_response_as(String jsonKey, String variableName) {
        String id = getJsonPath(response, jsonKey);
        assertNotNull(id);

        if (variableName.equalsIgnoreCase("boardId")) {
            boardId = id;
            String name = getJsonPath(response, "name");
            boardMap.put(name, id);
            System.out.println("Current Board Map: " + boardMap);
        } else if (variableName.equalsIgnoreCase("listId")) {
            String name = getJsonPath(response, "name");
            listMap.put(name, id);
            System.out.println("Current List Map: " + listMap);
        } else if (variableName.equalsIgnoreCase("labelId")) {
            String name = getJsonPath(response, "name");
            labelMap.put(name, id);
            System.out.println("Current Label Map: " + labelMap);
        } else if (variableName.equalsIgnoreCase("cardId")) {
            String name = getJsonPath(response, "name");
            cardMap.put(name, id);
            System.out.println("Current Card Map: " + cardMap);
        } else if (variableName.equalsIgnoreCase("checklistId")) {
            String name = getJsonPath(response, "name");
            checklistMap.put(name, id);
            System.out.println("Current Checklist Map: " + checklistMap);
        } else if (variableName.equalsIgnoreCase("checkItemId")) {
            String name = getJsonPath(response, "name");
            checkItemMap.put(name, id);
            System.out.println("Current CheckItem Map: " + checkItemMap);
        }

    }

    @Given("Ensure boards are available")
    public void ensure_boards_are_available() throws IOException {
        reqSpec = given()
                .spec(requestSpecification());
        user_calls_with_http_request("GetBoardAPI", "Get");
        List<String> ids = response.jsonPath().getList("id");
        List<String> names = response.jsonPath().getList("name");

        if (!ids.isEmpty()) {
            for (int i = 0; i < ids.size(); i++) {
                boardMap.put(names.get(i), ids.get(i));
            }
            System.out.println("Available board Map: " + boardMap);
        }

    }

    @Given("Update the board {string} with name {string} and color {string}")
    public void update_the_board_with_name_and_color(String currentName, String newName, String newColor) throws IOException {
        selectedBoardId = boardMap.get(currentName);
        if (selectedBoardId == null) {
            throw new IllegalStateException("Board with name " + currentName + " not found.");
        }
        System.out.println("selected " + selectedBoardId);
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedBoardId)
                .queryParam("name", newName)
                .queryParam("prefs/background", newColor);
    }

    @When("Delete the board {string}")
    public void delete_the_board(String boardName) throws IOException {
        selectedBoardId = boardMap.get(boardName);
        if (selectedBoardId == null) {
            throw new IllegalStateException("Board with name " + boardName + " not found.");
        }

        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedBoardId);

        APIResources resourceAPI = APIResources.DeleteBoardAPI;
        response = reqSpec.when().delete(resourceAPI.getResource());

        System.out.println("Deleted board ID: " + selectedBoardId);
        System.out.println("Status code: " + response.getStatusCode());

        assertEquals(200, response.getStatusCode());

        boardMap.remove(boardName);
        System.out.println("Remaining boards: " + boardMap);
    }

    @Then("board {string} should be deleted successfully")
    public void board_should_be_deleted_successfully(String boardName) {
        assertFalse(boardMap.containsKey(boardName));
    }

    @Given("Create List payload with {string} and board {string}")
    public void create_list_payload_with_and_board(String listName, String boardName) throws IOException {
        if (boardMap.isEmpty()) {
            throw new IllegalStateException("No boards available. Please create a board first.");
        }
        selectedBoardId = boardMap.get(boardName);
        if (selectedBoardId == null) {
            throw new IllegalStateException("Board with name " + boardName + " not found.");
        }

        reqSpec = given()
                .spec(requestSpecification())
                .queryParam("name", listName)
                .queryParam("idBoard", selectedBoardId);
    }

    @Given("Ensure lists are available")
    public void ensure_lists_are_available() throws IOException {
        if (listMap.isEmpty()) {
            ensure_boards_are_available();

            if (boardMap.isEmpty()) {
                throw new IllegalStateException("No boards available to create a list.");
            }
            String boardName = boardMap.keySet().iterator().next();  // Get any board name "which is one board created before ListTests"
            create_list_payload_with_and_board("E2E_Test_List_1", boardName);
            user_calls_with_http_request("CreateListAPI", "Post");
            String id = getJsonPath(response, "id");
            String name = getJsonPath(response, "name");
            listMap.put(name, id);
        }
        System.out.println("Available List Map: " + listMap);
    }

    @Given("Update the list {string} with name {string}")
    public void update_the_list_with_name(String currentName, String newName) throws IOException {
        selectedListId = listMap.get(currentName);
        if (selectedListId == null) {
            throw new IllegalStateException("List with name " + currentName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedListId)
                .queryParam("name", newName);
    }

    @Given("I target the list {string}")
    public void i_target_the_list(String listName) throws IOException {
        selectedListId = listMap.get(listName);
        if (selectedListId == null) {
            throw new IllegalStateException("List with name " + listName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedListId);
    }

    @Given("Archive the list {string}")
    public void archive_the_list(String listName) throws IOException {
        selectedListId = listMap.get(listName);
        if (selectedListId == null) {
            throw new IllegalStateException("List with name " + listName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedListId)
                .queryParam("closed", "true");
    }

    @Given("Delete all boards")
    public void delete_all_boards() throws IOException {
        ensure_boards_are_available();
        if (boardMap.isEmpty()) {
            System.out.println("No boards to delete.");
            return;
        }
        // Create a copy of keys to avoid ConcurrentModificationException
        List<String> boardNames = new ArrayList<>(boardMap.keySet());
        for (String name : boardNames) {
            delete_the_board(name);
        }
    }

    @Given("Create Label payload with {string} , {string} and board {string}")
    public void create_label_payload_with_and_board(String labelName, String labelColor, String boardName) throws IOException {
        if (boardMap.isEmpty()) {
            throw new IllegalStateException("No boards available. Please create a board first.");
        }
        selectedBoardId = boardMap.get(boardName);
        if (selectedBoardId == null) {
            throw new IllegalStateException("Board with name " + boardName + " not found.");
        }
        System.out.println("selected " + selectedBoardId);

        reqSpec = given()
                .spec(requestSpecification())
                .queryParam("name", labelName)
                .queryParam("color", labelColor)
                .queryParam("idBoard", selectedBoardId);

        System.out.println("Creating label for List Tests");
    }

    @Given("Ensure labels are available")
    public void ensure_labels_are_available() throws IOException {
        if (labelMap.isEmpty()) {
            ensure_boards_are_available();

            if (boardMap.isEmpty()) {
                throw new IllegalStateException("No boards available to create a label.");
            }
            String boardName = boardMap.keySet().iterator().next();  // Get any board name "which is one board created before LabelTests
            create_label_payload_with_and_board("E2E_Test_Label_1", "purple", boardName);
            user_calls_with_http_request("CreateLabelAPI", "Post");
            String id = getJsonPath(response, "id");
            String name = getJsonPath(response, "name");
            labelMap.put(name, id);
        }
        System.out.println("Available Label Map: " + labelMap);
    }
    @Given("retrieve the label with name {string}")
    public void retrieve_the_label_with_name(String labelName) throws IOException {
        selectedLabelId = labelMap.get(labelName);
        if (selectedLabelId == null) {
            throw new IllegalStateException("Label with name " + labelName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedLabelId);
    }

    @Given("Update the label {string} with name {string}")
    public void update_the_label_with_name(String labelName, String newName) throws IOException {
        selectedLabelId = labelMap.get(labelName);
        if (selectedLabelId == null) {
            throw new IllegalStateException("Label with name " + labelName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedLabelId)
                .queryParam("name", newName);
    }

    @Given("delete the label {string}")
    public void delete_the_label(String labelName) throws IOException {
        selectedLabelId = labelMap.get(labelName);
        if (selectedLabelId == null) {
            throw new IllegalStateException("Label with name " + labelName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedLabelId);
    }

    // ==================== CARD STEP DEFINITIONS ====================

    @Given("Create Card payload with {string} and list {string}")
    public void create_card_payload_with_and_list(String cardName, String listName) throws IOException {
        if (listMap.isEmpty()) {
            throw new IllegalStateException("No lists available. Please create a list first.");
        }
        selectedListId = listMap.get(listName);
        if (selectedListId == null) {
            throw new IllegalStateException("List with name " + listName + " not found.");
        }

        reqSpec = given()
                .spec(requestSpecification())
                .queryParam("name", cardName)
                .queryParam("idList", selectedListId);
    }

    @Given("Ensure cards are available")
    public void ensure_cards_are_available() throws IOException {
        if (cardMap.isEmpty()) {
            ensure_lists_are_available();

            if (listMap.isEmpty()) {
                throw new IllegalStateException("No lists available to create a card.");
            }
            String listName = listMap.keySet().iterator().next();
            create_card_payload_with_and_list("E2E_Test_Card_1", listName);
            user_calls_with_http_request("CreateCardAPI", "Post");
            String id = getJsonPath(response, "id");
            String name = getJsonPath(response, "name");
            cardMap.put(name, id);
        }
        System.out.println("Available Card Map: " + cardMap);
    }

    @Given("I target the card {string}")
    public void i_target_the_card(String cardName) throws IOException {
        selectedCardId = cardMap.get(cardName);
        if (selectedCardId == null) {
            throw new IllegalStateException("Card with name " + cardName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedCardId);
    }

    @Given("Update the card {string} with name {string} and description {string}")
    public void update_the_card_with_name_and_description(String cardName, String newName, String description) throws IOException {
        selectedCardId = cardMap.get(cardName);
        if (selectedCardId == null) {
            throw new IllegalStateException("Card with name " + cardName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedCardId)
                .queryParam("name", newName)
                .queryParam("desc", description);
    }

    @Given("delete the card {string}")
    public void delete_the_card(String cardName) throws IOException {
        selectedCardId = cardMap.get(cardName);
        if (selectedCardId == null) {
            throw new IllegalStateException("Card with name " + cardName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedCardId);
    }

    // ==================== CHECKLIST STEP DEFINITIONS ====================

    @Given("Create Checklist payload with {string} on card {string}")
    public void create_checklist_payload_with_on_card(String checklistName, String cardName) throws IOException {
        if (cardMap.isEmpty()) {
            throw new IllegalStateException("No cards available. Please create a card first.");
        }
        selectedCardId = cardMap.get(cardName);
        if (selectedCardId == null) {
            throw new IllegalStateException("Card with name " + cardName + " not found.");
        }

        reqSpec = given()
                .spec(requestSpecification())
                .queryParam("name", checklistName)
                .queryParam("idCard", selectedCardId);
    }

    @Given("Ensure checklists are available")
    public void ensure_checklists_are_available() throws IOException {
        if (checklistMap.isEmpty()) {
            ensure_cards_are_available();

            if (cardMap.isEmpty()) {
                throw new IllegalStateException("No cards available to create a checklist.");
            }
            String cardName = cardMap.keySet().iterator().next();
            create_checklist_payload_with_on_card("E2E_Test_Checklist_1", cardName);
            user_calls_with_http_request("CreateChecklistAPI", "Post");
            String id = getJsonPath(response, "id");
            String name = getJsonPath(response, "name");
            checklistMap.put(name, id);
        }
        System.out.println("Available Checklist Map: " + checklistMap);
    }

    @Given("I target the checklist {string}")
    public void i_target_the_checklist(String checklistName) throws IOException {
        selectedChecklistId = checklistMap.get(checklistName);
        if (selectedChecklistId == null) {
            throw new IllegalStateException("Checklist with name " + checklistName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedChecklistId);
    }

    @Given("Update the checklist {string} with name {string}")
    public void update_the_checklist_with_name(String checklistName, String newName) throws IOException {
        selectedChecklistId = checklistMap.get(checklistName);
        if (selectedChecklistId == null) {
            throw new IllegalStateException("Checklist with name " + checklistName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedChecklistId)
                .queryParam("name", newName);

        checklistMap.put(newName, selectedChecklistId);
        checklistMap.remove(checklistName);

    }

    @Given("delete the checklist {string}")
    public void delete_the_checklist(String checklistName) throws IOException {
        selectedChecklistId = checklistMap.get(checklistName);
        if (selectedChecklistId == null) {
            throw new IllegalStateException("Checklist with name " + checklistName + " not found.");
        }
        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedChecklistId);
    }

    @Given("Create CheckItem payload with {string} on checklist {string}")
    public void create_checkitem_payload_with_on_checklist(String checkItemName, String checklistName) throws IOException {
        if (checklistMap.isEmpty()) {
            throw new IllegalStateException("No checklists available. Please create a checklist first.");
        }
        selectedChecklistId = checklistMap.get(checklistName);
        if (selectedChecklistId == null) {
            throw new IllegalStateException("Checklist with name " + checklistName + " not found.");
        }

        reqSpec = given()
                .spec(requestSpecification())
                .pathParam("id", selectedChecklistId)
                .queryParam("name", checkItemName);
    }
}
