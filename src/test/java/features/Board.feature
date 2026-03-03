@BoardCRUD
Feature: Board Management


  @CreateBoard @Regression
  Scenario: Create board and verify it is persisted
    When I create a "board" with name "CreateBoardName"
    Then the status code should be 200
    And the response contains a non-empty "id"
    And the response name should be "CreateBoardName"
    When I get the "board" by id
    Then the status code should be 200
    And the response name should be "CreateBoardName"

  @UpdateBoard @Regression
  Scenario: Update board name and verify persisted update
    Given a "board" exists with name "CreateBoardName"
    When I update the "board" name to "UpdatedBoardName"
    Then the status code should be 200
    And the response name should be "UpdatedBoardName"
    When I get the "board" by id
    Then the status code should be 200
    And the response name should be "UpdatedBoardName"

  @DeleteBoard @Regression
  Scenario: Delete board and verify non-existence
    Given a "board" exists with name "BoardToDelete"
    When I delete the "board"
    Then the status code should be 200
    When I get the deleted "board" by id
    Then the status code should be 404

  @E2EBoardLifecycle @Regression
  Scenario: Full board lifecycle (create → update → delete) - run sparingly
    When I create a "board" with name "E2E_Board"
    Then the status code should be 200
    When I update the "board" name to "E2E_Board_Updated"
    Then the status code should be 200
    When I delete the "board"
    Then the status code should be 200
    When I get the deleted "board" by id
    Then the status code should be 404

  @Negative @Regression
  Scenario: Create board without required fields returns error
    When I create a "board" with an empty payload
    Then the status code should be 400
