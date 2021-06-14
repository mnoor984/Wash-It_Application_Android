Feature: View ads sorted by postal code

  As a user
  I would like to view ads sorted by postal code, in alphabetical order,
  So that I can find a group of ads in a region of my choice.

  Scenario: A user chooses to view ads sorted by postal code (Main flow)
    Given I am a user
    And I am viewing all the ads in the system
    When I choose the option to sort ads by postal code
    Then A list of ads, sorted by postal code, is given to me

  Scenario: a user chooses to not views ads sorted by postal code (alternate flow)
    Given I am a user
    And I am viewing all the ads in the system
    And they are sorted by postal code
    When I choose the option to unsort ads by postal code
    Then A list of ads, sorted by most recent, is given to me
