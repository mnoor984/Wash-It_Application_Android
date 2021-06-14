Feature: Modify Ad

  As a Customer of WashIt, I want to modify an ad I posted earlier so that washers are able to see the updated ad

  Background:

    Given I am a Customer of WashIt
    And I have posted an ad

  Scenario: Modify clothing description (Normal Flow)

    When  I enter the new clothing description
    Then The system will update the clothing description on the ad

  Scenario: Modify phone number (Alternate Flow)

    When I enter a new phone number
    Then The system will update the phone number on the specific ad

  Scenario: Modification fails as the drop-off window is before the pick-up window (Error Flow)

    When I enter a drop-off window that comes before the pick-up window
    Then I should see an error message


