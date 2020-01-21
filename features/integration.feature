Feature: System features

  Scenario: User requests registry
    Given The customer is not already registerered
    When The customer registers himself
    Then The customer will be registered
    And The customer cannot register himself again

  Scenario: User request tokens
    Given The customer has 0 tokens
    When The customer requests tokens
    Then The customer has 6 tokens

    Scenario: User is deleted
      Given The customer registers himself
      When The customer deletes himself
      Then The customer does not exist

  Scenario: Money transfer
    Given The customer has at least 1 token
    And The customer has a bank account
    And The merchant has a bank account
    And The customer has "100" currency in the bank
    And The merchant has "100" currency in the bank
    When The customer transfers 100 currency to the merchant
    Then The customer has "0.0" currency in the bank
    And The merchant has "200.0" currency in the bank