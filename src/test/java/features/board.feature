Feature: Validating Trello Board APIs

  @CreateBoard @Regression
  Scenario Outline: Verify that a new Trello board is created successfully
    Given Create Board payload with "<boardName>"
    When user calls "CreateBoardAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "<boardName>"
    And store "id" from response as "boardId"

    Examples:
      | boardName        |
      | E2E_Test_Board_1 |
      | E2E_Test_Board_2 |


  @UpdateBoard @Regression
  Scenario Outline: Update the board
    Given Ensure boards are available
    And Update the board "<boardName>" with name "<updatedBoardName>" and color "<color>"
    When user calls "UPDATEBoardAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "<updatedBoardName>"
    And store "id" from response as "boardId"

    Examples:
      | boardName        | updatedBoardName    | color  |
      | E2E_Test_Board_1 | Updated_Board_Name1 | orange |
      | E2E_Test_Board_2 | Updated_Board_Name2 | red    |


  @GetBoards @Regression
  Scenario: Retrieve all boards for the project
    Given Ensure boards are available
    When user calls "GetBoardAPI" with "GET" http request
    Then the API call is success with status code 200


  @DeleteBoard @Regression
  Scenario Outline: Delete the board
    Given Ensure boards are available
    When Delete the board "<updatedBoardName>"
    Then board "<updatedBoardName>" should be deleted successfully

    Examples:
      | updatedBoardName    |
      | Updated_Board_Name1 |
      | Updated_Board_Name2 |

