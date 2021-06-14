Feature: View previous ads posted by a customer

  As a user
  I would like to view ads of a particular user account of wash-it
  So that I can see the ad history of that user acount

  Background:
    Given that there is an acount for Wash-It with the username "AdPoster_A"
    And that the account with username "AdPoster_A" initially has no ads
    And that the account with username "AdPoster_A" posts an Ad with adId 712
    And that the account with username "AdPoster_A" posts an Ad with adId 713
    And that there is an acount for Wash-It with the username "AdPoster_B"
    And that the account with username "AdPoster_B" initially has no ads
    And that there is not an account for Wash-It with the name "AdPoster_Nonexistent"

  Scenario: View the ads of an user account that has ads (Normal Flow):
    When I request to view ads of an user account
    And I supply "AdPoster_A" as the username
    Then the ads with adIds 712 and 712 are returned

  Scenario: View the ads of an user account that has no ads (Alternate Flow):
    When I request to view ads of an user account
    And I supply "AdPoster_B" as the username
    Then no ads are returned
    And the message "No ads posted by the specific customer" is returned

  Scenario: View the ads of an user account that doesn't exist (Error Flow):
    When I request to view ads of an user account
    And I supply "AdPoster_Nonexistent" as the username
    Then no ads are returned
    And the message "account does not exist" is returned
