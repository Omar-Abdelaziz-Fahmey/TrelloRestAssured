@ListCRUD
Feature: List Management



  @CreateList @Regression
  Scenario: Create list and verify it is persisted
    Given a "board" exists with name "Parent_Board_For_Lists"
    When I create a "list" with name "Backlog_List"
    Then the status code should be 200
    And the response name should be "Backlog_List"
    When I get the "list" by id
    Then the status code should be 200

  @UpdateList @Regression
  Scenario: Update list name
    Given a "list" exists with name "Backlog_List"
    When I update the "list" name to "New_List_Name"
    Then the status code should be 200
    And the response name should be "New_List_Name"

  @DeleteList @Regression
    Scenario: Archive list and delete its parent board
    Given a "list" exists with name "New_List_Name"
    When I delete the "list"
    Then the status code should be 200
    And I delete the "board"
    Then the status code should be 200

  @E2EListLifecycle @Regression
  Scenario: Full list lifecycle (create -> update -> delete)
    Given a "board" exists with name "E2E_List_Lifecycle_Board"
    When I create a "list" with name "Initial_List_Name"
    Then the status code should be 200
    When I update the "list" name to "Updated_List_Name"
    Then the status code should be 200
    And the response name should be "Updated_List_Name"
    When I delete the "list"
    Then the status code should be 200
    And I delete the "board"
    Then the status code should be 200
  @Negative @Regression
    Scenario: Create list without required fields returns error
    Given a "board" exists with name "Parent_Board_For_Lists"
    When I create a "list" with an empty payload
    Then the status code should be 400

