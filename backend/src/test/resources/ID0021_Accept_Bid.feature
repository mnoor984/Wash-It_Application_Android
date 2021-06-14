Feature: Accept a bid on an ad

 As a user
 I would like to accept a bid on my existing ad
 So that I can receive a washing service from another user

 Background:
  Given I am a user of Wash-it
  And I logged into my account
  And I have posted a ad
  And there are at least two bids on my ad
		
 Scenario: Accept one bid successfully (normal flow)
  When I accept a bid on my ad 
  Then that ad will have an accepted bid

 Scenario: Try to accept more than one bid (error flow)
  Given I already have an accepted bid on my ad
  When I try to accept another bid
  Then an error message "Ad already has accepted bid" will be displayed
