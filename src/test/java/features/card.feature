@CardTests
Feature: Validating Trello Card APIs

  @CreateCard @Regression
  Scenario Outline: Verify that a new Trello card is created successfully
    Given Ensure lists are available
    And Create Card payload with "<cardName>" and list "E2E_Test_List_1"
    When user calls "CreateCardAPI" with "POST" http request
    Then the API call is success with status code 200
    And "name" in response body is "<cardName>"
    And store "id" from response as "cardId"

    Examples:
      | cardName        |
      | E2E_Test_Card_1 |
      | E2E_Test_Card_2 |


  @GetCard @Regression
  Scenario Outline: Retrieve the created card
    Given Ensure cards are available
    And I target the card "<cardName>"
    When user calls "GetCardAPI" with "GET" http request
    Then the API call is success with status code 200
    And "name" in response body is "<cardName>"

    Examples:
      | cardName        |
      | E2E_Test_Card_1 |
      | E2E_Test_Card_2 |


  @UpdateCard @Regression
  Scenario Outline: Update the card name and description
    Given Ensure cards are available
    And Update the card "<cardName>" with name "<updatedCardName>" and description "<description>"
    When user calls "UpdateCardAPI" with "PUT" http request
    Then the API call is success with status code 200
    And "name" in response body is "<updatedCardName>"
    And store "id" from response as "cardId"

    Examples:
      | cardName        | updatedCardName    | description              |
      | E2E_Test_Card_1 | Updated_Card_Name1 | This is card description |
      | E2E_Test_Card_2 | Updated_Card_Name2 | Another card description |


  @DeleteCard @Regression
  Scenario Outline: Delete the card
    Given Ensure cards are available
    And delete the card "<updatedCardName>"
    When user calls "DeleteCardAPI" with "DELETE" http request
    Then the API call is success with status code 200

    Examples:
      | updatedCardName    |
      | Updated_Card_Name1 |
      | Updated_Card_Name2 |
