@ListTests
Feature: Validating Trello List APIs

  @CreateList @Regression
  Scenario Outline: Verify that a new Trello list is created successfully
    Given Ensure boards are available
    And Create List payload with "<listName>" and board "Board_For_Tests_Test"
    When user calls "CreateListAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "<listName>"
    And store "id" from response as "listId"

    Examples:
      | listName        |
      | E2E_Test_List_1 |
      | E2E_Test_List_2 |



  @GetList @Regression
  Scenario Outline: Retrieve the created list
    Given Ensure lists are available
    And I target the list "<listName>"
    When user calls "GetListAPI" with "GET" http request
    Then the API call is success with status code 200
    And "name" in response body is "<listName>"

    Examples:
      | listName        |
      | E2E_Test_List_1 |
      | E2E_Test_List_2 |


  @UpdateList @Regression
  Scenario Outline: Update the list name
    Given Ensure lists are available
    And Update the list "<listName>" with name "<updatedListName>"
    When user calls "UpdateListAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "<updatedListName>"
    And store "id" from response as "listId"

    Examples:
      | listName        | updatedListName    |
      | E2E_Test_List_1 | Updated_List_Name1 |
      | E2E_Test_List_2 | Updated_List_Name2 |


  @ArchiveList @Regression
  Scenario Outline: Archive the list
    Given Ensure lists are available
    And Archive the list "<updatedListName>"
    When user calls "ArchiveListAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "closed" in response body is "true"

    Examples:
      | updatedListName    |
      | Updated_List_Name1 |
      | Updated_List_Name2 |
