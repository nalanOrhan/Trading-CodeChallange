Feature: Functional Tests
  Scenario Outline: Creating user successfully
    Given a valid user name provided "<name>"
    When a post request to create user send to API
    And receive the response and validate the user data
    Then send a get request to list all users and validate the user exist

    Examples:
      | name   |
      | Silver |
      | Falcon |

  Scenario: Creating Security successfully
    Given a valid security name provided "TVN"
    When a post request to create security send to API
    And receive the response and validate the security data
    Then send a get request to list all users and validate the security exist

  Scenario: Creating a BUY order successfully
    Given an exist security "TVN", an exist user "Silver", quantity 101, price 60
    When a post request to create "buy" order send to API
    And receive the response and validate the order data
    Then send a get request to list all orders and validate the "buy" order exist

  Scenario: Creating a SELL order successfully
    Given an exist security "TVN", an exist user "Silver", quantity 100, price 70
    When a post request to create "sell" order send to API
    And receive the response and validate the order data
    Then send a get request to list all orders and validate the "sell" order exist

