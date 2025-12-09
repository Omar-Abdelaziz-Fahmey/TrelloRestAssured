@ChecklistTests
Feature: Validating Trello Checklist APIs

  @CreateChecklist @Regression
  Scenario Outline: Verify that a new Trello checklist is created successfully
    Given Ensure cards are available
    And Create Checklist payload with "<checklistName>" on card "E2E_Test_Card_1"
    When user calls "CreateChecklistAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "<checklistName>"
    And store "id" from response as "checklistId"

    Examples:
      | checklistName        |
      | E2E_Test_Checklist_1 |
      | E2E_Test_Checklist_2 |

  @GetChecklist @Regression
  Scenario Outline: Retrieve the created checklist
    Given Ensure checklists are available
    And I target the checklist "<checklistName>"
    When user calls "GetChecklistAPI" with "GET" http request
    Then the API call is success with status code 200
    And "name" in response body is "<checklistName>"

    Examples:
      | checklistName        |
      | E2E_Test_Checklist_1 |
      | E2E_Test_Checklist_2 |

  @UpdateChecklist @Regression
  Scenario Outline: Update the checklist name
    Given Ensure checklists are available
    And Update the checklist "<checklistName>" with name "<updatedChecklistName>"
    When user calls "UpdateChecklistAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "<updatedChecklistName>"
    And store "id" from response as "checklistId"

    Examples:
      | checklistName        | updatedChecklistName    |
      | E2E_Test_Checklist_1 | Updated_Checklist_Name1 |
      | E2E_Test_Checklist_2 | Updated_Checklist_Name2 |

  @CreateCheckItem @Regression
  Scenario Outline: Add a check item to the checklist
    Given Ensure checklists are available
    And Create CheckItem payload with "<checkItemName>" on checklist "Updated_Checklist_Name1"
    When user calls "CreateCheckItemAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "<checkItemName>"
    And store "id" from response as "checkItemId"

    Examples:
      | checkItemName   |
      | E2E_Test_Item_1 |
      | E2E_Test_Item_2 |

  @DeleteChecklist @Regression
  Scenario Outline: Delete the checklist
    Given Ensure checklists are available
    And delete the checklist "<updatedChecklistName>"
    When user calls "DeleteChecklistAPI" with "DELETE" http request
    Then the API call is success with status code 200

    Examples:
      | updatedChecklistName    |
      | Updated_Checklist_Name1 |
      | Updated_Checklist_Name2 |
