Feature: This should describe the feature of displaying books

  Scenario: Retrieve books
    Given user authenticated and clicked on the market
    Then books are shown
    Then one book has the id 'details1001'

   Scenario: Retrieve no books
     Given user authenticated and clicked on the market
     Then no books are shown