Feature: View All Ads

  As a Washer wanting to provide laundry services,
  I would like to view all ads demanding laundry
  services on the Wash-It platform so that I can
  identify ads that interest me.

  Background:

    Given I am currently logged in as a Washer
    And there are currently no ads on the Wash-It platform

  Scenario: View all ads when there exist multiple ads (normal flow)

  I should get all ads

    Given an ad with UID 111 is created
    And an ad with UID 112 is created
    When I view ads
    Then two ads with UID 111 and UID 112 should be returned

  Scenario: View all ads when there is only one ad (alternate flow)

  I should get one ad

    Given an ad with UID 111 is created
    When I view ads
    Then one ad with UID 111 should be returned

  Scenario: View all ads when there is no ad (error flow)

  I should get an error message

    When I view ads
    Then a "there are currently no ads" error message is issed
