Feature: Validating Trello Label APIs

  @CreateLabel @Regression @Label
  Scenario Outline: Verify that a new Trello label is created successfully
    Given Ensure boards are available
    And Create Label payload with "<labelName>" , "<labelColor>" and board "Board_For_Tests_Test"
    When user calls "CreateLabelAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "<labelName>"
    And store "id" from response as "labelId"

    Examples:
      | labelName        | labelColor |
      | E2E_Test_Label_1 | purple     |
      | E2E_Test_Label_2 | blue       |


  @GetLabel @Regression @Label
  Scenario Outline: Retrieve the created label
    Given Ensure labels are available
    And retrieve the label with name "<labelName>"
    When user calls "GetLabelAPI" with "GET" http request
    Then the API call is success with status code 200
    And "name" in response body is "<labelName>"

    Examples:
      | labelName        |
      | E2E_Test_Label_1 |
      | E2E_Test_Label_2 |


  @UpdateLabel @Regression @Label
  Scenario Outline: Update the label name
    Given Ensure labels are available
    And Update the label "<labelName>" with name "<updatedLabelName>"
    When user calls "UpdateLabelAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "<updatedLabelName>"
    And store "id" from response as "labelId"

    Examples:
      | labelName        | updatedLabelName    |
      | E2E_Test_Label_1 | Updated_Label_Name1 |
      | E2E_Test_Label_2 | Updated_Label_Name2 |


  @DeleteLabel @Regression @Label
  Scenario Outline: Delete the label
    Given Ensure labels are available
    And delete the label "<updatedLabelName>"
    When user calls "DeleteLabelAPI" with "DELETE" http request
    Then the API call is success with status code 200

    Examples:
      | updatedLabelName    |
      | Updated_Label_Name1 |
      | Updated_Label_Name2 |
