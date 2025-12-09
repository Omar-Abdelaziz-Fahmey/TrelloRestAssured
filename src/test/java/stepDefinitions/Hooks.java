package stepDefinitions;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;


import java.io.IOException;

public class Hooks {

    @Before("@ListTests or @Label or @CardTests or @ChecklistTests")
    public void beforeListTests() throws IOException {

        StepDefinition stepDef = new StepDefinition();
        stepDef.ensure_boards_are_available();
        if (StepDefinition.boardMap.containsKey("Board_For_Tests_Test")) {
            // Board already exists, no need to create a new one
            return;
        }
        stepDef.create_board_payload_with("Board_For_Tests_Test");
        stepDef.user_calls_with_http_request("CreateBoardAPI", "Post");
        // Clear existing boards and store only the new one
        StepDefinition.boardMap.clear();
        stepDef.store_from_response_as("id", "boardId");

    }

    @AfterAll
    public static void afterListTests() throws IOException {

        StepDefinition stepDef = new StepDefinition();
        if (!StepDefinition.boardMap.isEmpty()) {
            // Since we clear the map in beforeListTests, there should be only one or we know the name
            stepDef.delete_the_board("Board_For_Tests_Test");
            StepDefinition.listMap.clear();
            StepDefinition.cardMap.clear();
            StepDefinition.checklistMap.clear();
            StepDefinition.checkItemMap.clear();
        }

    }

}
