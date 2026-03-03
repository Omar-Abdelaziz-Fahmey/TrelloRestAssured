@CardCRUD
Feature: Card Management

  @CreateCard @Regression
  Scenario: Create card and verify it is persisted
    Given a "board" exists with name "Parent_Board_For_Cards"
    And a "list" exists with name "Parent_List_For_Cards"
    When I create a "card" with name "Card_To_Create"
    Then the status code should be 200
    And the response contains a non-empty "id"
    And the response name should be "Card_To_Create"
    When I get the "card" by id
    Then the status code should be 200
    And the response name should be "Card_To_Create"

  @UpdateCard @Regression
  Scenario: Update card name and verify persistence
    Given a "board" exists with name "Parent_Board_For_Cards"
    And a "card" exists with name "Card_To_Create"
    When I update the "card" name to "Card_To_Update"
    Then the status code should be 200
    And the response name should be "Card_To_Update"
    When I get the "card" by id
    Then the status code should be 200
    And the response name should be "Card_To_Update"

  @DeleteCard @Regression
  Scenario: Delete card and verify non-existence
    Given a "board" exists with name "Parent_Board_For_Cards"
    And a "card" exists with name "Task_To_Delete"
    When I delete the "card"
    Then the status code should be 200
    When I get the deleted "card" by id
    Then the status code should be 404

  @E2ECardLifecycle @Regression
  Scenario: Full card lifecycle within a list
    Given a "list" exists with name "Task_Lifecycle_List"
    When I create a "card" with name "E2E_Card"
    Then the status code should be 200
    When I update the "card" name to "E2E_Card_Final"
    Then the status code should be 200
    When I delete the "card"
    Then the status code should be 200
    And I delete the "board"
    Then the status code should be 200

  @Negative @Regression
  Scenario: Create card without required fields returns error
    Given a "board" exists with name "Parent_Board_For_Cards"
    And a "list" exists with name "Error_List"
    When I create a "card" with an empty payload
    Then the status code should be 400