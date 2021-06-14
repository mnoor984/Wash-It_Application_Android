Feature: Log out of currently logged in account

As a user
I want to log out of my account 
so that my account is secure from people who can access my phone

  Background: 
  
    Given I am a user of Wash-it
      And I am signed into my account
  
  Scenario: I successfully log out of my account (Normal Flow)
  
     When I request to log out of my account
      And I confirm that I want to log out of my account
     Then the logged in status for my account becomes false
  
  Scenario: I don't log out of my account because I cancel confirmation to log out my account (Error Flow)
  
     When I request to log out of my account
      And I cancel confirmation to log out of my account
      Then the logged in status for my account remains true
  
  
