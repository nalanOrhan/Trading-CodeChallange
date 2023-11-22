@FunctionalTests
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
    Given an exist security, an exist user, quantity "101", price "60"
    When a post request to create "BUY" order send to API
    And receive the response and validate the order data
    Then send a get request to list all orders and validate the "BUY" order exist

  Scenario: Creating a SELL order successfully
    Given an exist security, an exist user, quantity "100", price "70"
    When a post request to create "SELL" order send to API
    And receive the response and validate the order data
    Then send a get request to list all orders and validate the "SELL" order exist

  Scenario: Validation of the trades
    Given a Buy order and a Sell order exist for same security from different users
    When a get request sent to with sell order id and buy order id to API
    Then receive the response and validate the trade data

