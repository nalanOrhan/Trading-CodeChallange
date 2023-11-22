@SmokeTests
Feature: 00 Smoke Tests

  Scenario: Basic trading Buy Sell
    Given one security "WSB" and two users "Diamond" and "Paper" exist
    When user "Diamond" puts a "buy" order for security "WSB" with a price of 101 and quantity of 50
    And user "Paper" puts a "sell" order for security "WSB" with a price of 100 and a quantity of 100
    Then a trade occurs with the price of 100 and quantity of 50

  Scenario: Basic trading Sell Buy
    Given one security "SEC" and two users "User1" and "User2" exist
    When user "User2" puts a "sell" order for security "SEC" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "SEC" with a price of 101 and quantity of 50
    Then a trade occurs with the price of 100 and quantity of 50

  Scenario: No trades occur
    Given one security "NTR" and two users "User1" and "User2" exist
    When user "User2" puts a "sell" order for security "NTR" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "NTR" with a price of 99 and quantity of 50
    Then no trades occur

  Scenario: Trade with two buy order
    Given one security "TBL" and three users "Fox", "Hawk", "Silver" exist
    When user "Hawk" puts a "sell" order for security "TBL" with a price of 65 and a quantity of 60
    And user "Fox" puts a "buy" order for security "TBL" with a price of 70 and quantity of 50
    Then a trade occurs with the price of 65 and quantity of 10
