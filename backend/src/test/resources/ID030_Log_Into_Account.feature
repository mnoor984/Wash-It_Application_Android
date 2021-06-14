Feature: Log into an Account
  
  As a user that wishes to access services of Wash-It
  I would like to be able to log into my account
  so that those services are assciated with my account

  Background: 
    Given that I have an account for Wash-It
    And that my username is "LoginTestAccount"
    And that my fullname is "Sudz Aldrain"
    And that my password is "Password123!"
    And the logged-in status of my account with username "LoginTestAccount" is false
    And that the user account with username "NonExistentAccount" does not exist in the system

  Scenario: Log into my account with correct username and password(Normal Flow)
    
    I should get logged in

    When I request to log in
    And I supply "LoginTestAccount" as username
    And I supply "Password123!" as password
    Then the authentication token is returned
    And the logged-in status of my account with username "LoginTestAccount" is true

  Scenario: Log into my account with correct username and incorrect password (Error Flow)
    
    I should not get logged in

    When I request to log in
    And I supply "LoginTestAccount" as username
    And I do not supply "Password123!" as password
    Then the error message "Incorrect password" is returned
    And the logged-in status of my account with username "LoginTestAccount" remains false

  Scenario: Log into my account with nonexistent username (Error Flow)
    
    I should not get logged in

    When I request to log in
    And I supply "NonExistentAccount" as username
    And I supply "Password123!" as password
    Then the error message "Account does not exist" is returned
    And the logged-in status of my account with username "LoginTestAccount" remains false
