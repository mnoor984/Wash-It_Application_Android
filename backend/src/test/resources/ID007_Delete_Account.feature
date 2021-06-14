Feature: Delete an Account

As a user of Wash-It
I would like to delete my account
So that the account no longer exists in the system

  Background: 
    Given I am logged in as an existing user of Wash-It
  
  Scenario: I successfully delete my account from Wash-It (Normal Flow)
     When I request to delete my account
     And I confirm to delete my account
     Then my account is removed from the system
  
  Scenario: I don't delete my account because I cancel the confirmation (Error Flow)
     When I request to delete my account
     And I cancel the confirmation to delete my account
     Then my account is not deleted
