Feature: Create Ad

  As a Customer
  I would like to create an ad
  So that I can pay a Washer to do my laundry for me

  Background:
    Given I am a Customer of WashIt

  Scenario: Create an ad (Normal Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    And   I choose services
    And   I enter special instructions
    Then  The system will create an ad with this information

  Scenario: Create an ad with minimum info (Alternate Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    Then  The system will create an ad with this information

  Scenario: Create an ad with missing phone number (Error Flow)

    When  I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    Then  an error message is displayed

  Scenario: Create an ad with missing clothing description (Error Flow)

    When  I enter a phone number
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    Then  an error message is displayed

  Scenario: Create an ad with missing pickup window (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    Then  an error message is displayed

  Scenario: Create an ad with missing dropoff window (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter an address
    And   I enter a zip code
    Then  an error message is displayed

  Scenario: Create an ad with missing address (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter a zip code
    Then  an error message is displayed

  Scenario: Create an ad with missing zip code (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    Then  an error message is displayed

  Scenario: Create an ad fails as the pickup window date is after the dropoff window date (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    And   the pickup window date is after the dropoff window date
    Then  an error message is displayed

  Scenario: Create an ad fails as the pickup window is too short (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    And   the pickup window is too short
    Then  an error message is displayed

  Scenario: Create an ad fails as the dropoff window is too short (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    And   the dropoff window is too short
    Then  an error message is displayed

  Scenario: Create an ad fails as the dropoff window is too close to the pickup window (Error Flow)

    When  I enter a phone number
    And   I enter a clothing description
    And   I enter a pickup window
    And   I enter a dropoff window
    And   I enter an address
    And   I enter a zip code
    And   the dropoff window is too close to the pickup window
    Then  an error message is displayed
