Feature: Create a bid on an Ad

	As a user
	I would like to make a bid on an existing ad
	So that I can provide a washing service for another user

	Background:
		Given I am a user
		And I am logged into my account
		And there exists an ad that is not posted by me
		
	Scenario: Bid on an ad successfully (normal flow)
		
		When I try to make a bid of a 100 dollars on that ad
		Then that ad will have a bid of a 100 dollars

	Scenario: Bid on an ad that I posted (error flow)
	
		Given I posted an ad
		When I try to bid on that ad
		Then the system shall display an error