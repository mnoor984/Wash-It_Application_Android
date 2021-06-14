Feature: View All Bids on Ad

  As a logged in user of Wash-It, 
  I would like to view all bids on any given ad
  So that I can identify bids that interest me

  Background:

    Given I am currently logged in as a Wash-It user
    And there is an ad with UID 111 with no bids

  Scenario: View all bids when there exists multiple bids (normal flow)
    
    I should get all bids

    Given a bid is made on the ad with UID 111
    And another bid on the ad with UID 111 is created
    When I view bids on the ad with UID 111
    Then the two bids should be returned

  Scenario: View all bids when there is only one bid (alternate flow)
    
    I should get one bid
    
    Given a bid is made on the ad with UID 111
    When I view bids on the ad with UID 111
    Then one bid should be returned

  Scenario: View all bids when there are no bids (error flow)
    
    I should get an error message
    
    When I view bids on the ad with UID 111
    Then a "there are currently no bids on this ad" message is issued

