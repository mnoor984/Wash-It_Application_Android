Feature: Accept a bid on an ad

 As a user
 I would like to accept a bid on my existing ad
 So that I can receive a washing service from another user

 Background:
  Given I am a user
  And I am logged into my account
  And I have posted an ad
  And there is a bid on my ad
		
 Scenario: Accept one bid successfully (normal flow)
  When I accept a bid on my ad 
  Then that ad will have an accepted bid

 Scenario: Try to accept more than one bid (error flow)
  When there is a second bid on my ad
  Then I accept a bid on my ad 
  Then that ad will have an accepted bid
  When I try to accept the second bid
  Then an error message "Ad already has accepted bid" is displayed
