Feature: View All Bids on Ad

  As a logged in user of Wash-It, 
  I would like to view all bids on any given ad
  So that I can identify bids that interest me

  Background:

    Given I am currently logged in as a Wash-It user
    And there is an ad with no bids

  Scenario: View all bids when there exists multiple bids (normal flow)
    
    I should get all bids

    And a bid is made on this ad
    And another bid on the same ad is created
    When I view bids on this ad
    Then the two bids should be returned

  Scenario: View all bids when there is only one bid (alternate flow)
    
    I should get one bid
    
    And a bid is made on this ad
    When I view bids on this ad
    Then one bid should be returned

  Scenario: View all bids when there are no bids (error flow)
    
    I should get an error message
    
    When I view bids on this ad
    Then a "there are currently no bids on this ad" error message is issued

