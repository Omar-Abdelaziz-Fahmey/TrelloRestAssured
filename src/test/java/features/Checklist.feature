@ChecklistCRUD
Feature: Checklist Management

  @CreateChecklist @Regression
  Scenario: Create checklist and verify it is persisted
    Given a "board" exists with name "Parent_Board_For_Checklists"
    And a "list" exists with name "Parent_List_For_Checklists"
    And a "card" exists with name "Parent_Card_For_Checklists"
    When I create a "checklist" with name "Checklist_To_Create"
    Then the status code should be 200
    And the response contains a non-empty "id"
    And the response name should be "Checklist_To_Create"
    When I get the "checklist" by id
    Then the status code should be 200
    And the response name should be "Checklist_To_Create"

  @UpdateChecklist @Regression
  Scenario: Update checklist name and verify persistence
    Given a "board" exists with name "Parent_Board_For_Checklists"
    And a "list" exists with name "Parent_List_For_Checklists"
    And a "card" exists with name "Parent_Card_For_Checklists"
    And a "checklist" exists with name "Checklist_To_Create"
    When I update the "checklist" name to "Checklist_To_Update"
    Then the status code should be 200
    And the response name should be "Checklist_To_Update"
    When I get the "checklist" by id
    Then the status code should be 200
    And the response name should be "Checklist_To_Update"

  @DeleteChecklist @Regression
  Scenario: Delete checklist and verify non-existence
    Given a "board" exists with name "Parent_Board_For_Checklists"
    And a "list" exists with name "Parent_List_For_Checklists"
    And a "card" exists with name "Parent_Card_For_Checklists"
    And a "checklist" exists with name "Checklist_To_Delete"
    When I delete the "checklist"
    Then the status code should be 200
    When I get the deleted "checklist" by id
    Then the status code should be 404

  @E2EChecklistLifecycle @Regression
  Scenario: Full checklist lifecycle within a card
    Given a "board" exists with name "Checklist_Lifecycle_Board"
    And a "list" exists with name "Checklist_Lifecycle_List"
    And a "card" exists with name "Checklist_Lifecycle_Card"
    When I create a "checklist" with name "E2E_Checklist"
    Then the status code should be 200
    When I update the "checklist" name to "E2E_Checklist_Final"
    Then the status code should be 200
    When I delete the "checklist"
    Then the status code should be 200
    And I delete the "card"
    Then the status code should be 200
    And I delete the "board"
    Then the status code should be 200

  @Negative @Regression
  Scenario: Create checklist without required fields returns error
    Given a "board" exists with name "Parent_Board_For_Checklists"
    And a "list" exists with name "Error_List_For_Checklists"
    And a "card" exists with name "Error_Card_For_Checklists"
    When I create a "checklist" with an empty payload
    Then the status code should be 400