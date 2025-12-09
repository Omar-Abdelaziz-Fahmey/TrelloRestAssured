@E2E
Feature: End-to-End Trello API Workflow Tests

  @E2E_FullBoardWorkflow @Regression
  Scenario: Complete Board workflow with List, Card, Checklist, and CheckItems
    # Step 1: Create a new Board
    Given Create Board payload with "E2E_Workflow_Board"
    When user calls "CreateBoardAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Workflow_Board"
    And store "id" from response as "boardId"
    # Step 2: Create a List on the Board
    Given Create List payload with "E2E_Workflow_List" and board "E2E_Workflow_Board"
    When user calls "CreateListAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Workflow_List"
    And store "id" from response as "listId"
    # Step 3: Create a Card on the List
    Given Create Card payload with "E2E_Workflow_Card" and list "E2E_Workflow_List"
    When user calls "CreateCardAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Workflow_Card"
    And store "id" from response as "cardId"
    # Step 4: Create a Checklist on the Card
    Given Create Checklist payload with "E2E_Workflow_Checklist" on card "E2E_Workflow_Card"
    When user calls "CreateChecklistAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Workflow_Checklist"
    And store "id" from response as "checklistId"
    # Step 5: Add CheckItem to the Checklist
    Given Create CheckItem payload with "E2E_Workflow_CheckItem" on checklist "E2E_Workflow_Checklist"
    When user calls "CreateCheckItemAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Workflow_CheckItem"
    And store "id" from response as "checkItemId"
    # Step 6: Verify the Card exists
    Given I target the card "E2E_Workflow_Card"
    When user calls "GetCardAPI" with "GET" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Workflow_Card"
    # Step 7: Delete the Board (cleanup)
    When Delete the board "E2E_Workflow_Board"
    Then board "E2E_Workflow_Board" should be deleted successfully

  @E2E_MultiCardWorkflow @Regression
  Scenario: Create Board with multiple Cards and Labels
    # Step 1: Create a new Board
    Given Create Board payload with "E2E_MultiCard_Board"
    When user calls "CreateBoardAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_MultiCard_Board"
    And store "id" from response as "boardId"
    # Step 2: Create a List
    Given Create List payload with "E2E_MultiCard_List" and board "E2E_MultiCard_Board"
    When user calls "CreateListAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_MultiCard_List"
    And store "id" from response as "listId"
    # Step 3: Create first Card
    Given Create Card payload with "E2E_Card_1" and list "E2E_MultiCard_List"
    When user calls "CreateCardAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Card_1"
    And store "id" from response as "cardId"
    # Step 4: Create second Card
    Given Create Card payload with "E2E_Card_2" and list "E2E_MultiCard_List"
    When user calls "CreateCardAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Card_2"
    And store "id" from response as "cardId"
    # Step 5: Create a Label
    Given Create Label payload with "E2E_Label_Priority" , "red" and board "E2E_MultiCard_Board"
    When user calls "CreateLabelAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Label_Priority"
    And store "id" from response as "labelId"
    # Step 6: Update first Card with description
    Given Update the card "E2E_Card_1" with name "E2E_Card_1_Updated" and description "High priority task"
    When user calls "UpdateCardAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Card_1_Updated"
    # Step 7: Update second Card with description
    Given Update the card "E2E_Card_2" with name "E2E_Card_2_Updated" and description "Low priority task"
    When user calls "UpdateCardAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "E2E_Card_2_Updated"
    # Step 8: Delete the Board (cleanup)
    When Delete the board "E2E_MultiCard_Board"
    Then board "E2E_MultiCard_Board" should be deleted successfully

  @E2E_ChecklistManagement @Regression
  Scenario: Complete Checklist management workflow
    # Step 1: Create a new Board
    Given Create Board payload with "E2E_Checklist_Board"
    When user calls "CreateBoardAPI" with "POST" http request
    Then the API call is success with status code 200
    And store "id" from response as "boardId"
    # Step 2: Create a List
    Given Create List payload with "E2E_Checklist_List" and board "E2E_Checklist_Board"
    When user calls "CreateListAPI" with "POST" http request
    Then the API call is success with status code 200
    And store "id" from response as "listId"
    # Step 3: Create a Card
    Given Create Card payload with "E2E_Checklist_Card" and list "E2E_Checklist_List"
    When user calls "CreateCardAPI" with "POST" http request
    Then the API call is success with status code 200
    And store "id" from response as "cardId"
    # Step 4: Create first Checklist
    Given Create Checklist payload with "Todo_Checklist" on card "E2E_Checklist_Card"
    When user calls "CreateChecklistAPI" with "POST" http request
    Then the API call is success with status code 200
    And store "id" from response as "checklistId"
    # Step 5: Create second Checklist
    Given Create Checklist payload with "Done_Checklist" on card "E2E_Checklist_Card"
    When user calls "CreateChecklistAPI" with "POST" http request
    Then the API call is success with status code 200
    And store "id" from response as "checklistId"
    # Step 6: Add CheckItems to first Checklist
    Given Create CheckItem payload with "Task_1" on checklist "Todo_Checklist"
    When user calls "CreateCheckItemAPI" with "POST" http request
    Then the API call is success with status code 200
    And store "id" from response as "checkItemId"
    Given Create CheckItem payload with "Task_2" on checklist "Todo_Checklist"
    When user calls "CreateCheckItemAPI" with "POST" http request
    Then the API call is success with status code 200
    And store "id" from response as "checkItemId"
    # Step 7: Update first Checklist name
    Given Update the checklist "Todo_Checklist" with name "In_Progress_Checklist"
    When user calls "UpdateChecklistAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "In_Progress_Checklist"
    # Step 8: Verify Checklist was updated
    Given I target the checklist "In_Progress_Checklist"
    When user calls "GetChecklistAPI" with "GET" http request
    Then the API call is success with status code 200
    And "name" in response body is "In_Progress_Checklist"
    # Step 9: Delete the Board (cleanup)
    When Delete the board "E2E_Checklist_Board"
    Then board "E2E_Checklist_Board" should be deleted successfully
