Feature: View user profile

As a user of Wash-It
I would like to view another users profile
So that I can see the users basic account information

  Background: 
  
    Given I am logged into Wash-It
  
  Scenario: Successfully view a users profile (Normal flow)
  
      And There exists another user of Wash-It
     When I request to view a users profile
     Then The users profile information should be returned
  
  Scenario: Attempt to view the profile of a user that no longer exists (Error Flow)
  
     When I request to view the profile of a user that deleted his or her account
     Then The system should return an error
  
  
