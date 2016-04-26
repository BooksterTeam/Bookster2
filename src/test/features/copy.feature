Feature: This should describe the feature of adding a copy

  Scenario: Add copy
    Given user authenticated and navigated to the copys
    Given a modal does pop up
    Given add a copy for the book with the id '1001'
    Then a copy has been added
