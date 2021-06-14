Feature: Create an Account

  As a person in need of laundry services or wanting to provide laundry services
  I would like create an account on Wash-It
  So that I can create ads or respond to ads for laundry services

  Background:

    Given I do not already have an account for Wash-It

  Scenario: Create an account and all fields are valid (Normal Flow)

  I should get logged in and recieve a confirmation email

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "password123!"
    And I fill in the confirm password field with "password123!"
    Then I should get logged in
    And I should recieve a confirmation email

  Scenario: Create an account but the full name field is invalid (Error Flow)

  I should not be able to creat an account with an invalid name

    When I fill in the full name field with ""
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "password123!"
    And I fill in the confirm password field with "password123!"
    Then an error message "Please enter a valid name" is displayed

  Scenario: Create an account but the email is invalid (Error Flow)

  I should not be able to create an account with an invalid email

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "An Invalid Email"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "password123!"
    And I fill in the confirm password field with "password123!"
    Then an error message "Email is invalid" is displayed

  Scenario: Create an account but an account with the given email already exists (Error Flow)

  I should not be able to create an account if an account with the same email already exists

    Given an account with the email "wash.maclothes@gmail.com" already exists
    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "password123!"
    And I fill in the confirm password field with "password123!"
    Then an error message "An account with that email already exists" is displayed

  Scenario: Create an account but the username field is invalid (Error Flow)

  I should not be able to create an account with an invalid username

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with ""
    And I fill in the password field with "password123!"
    And I fill in the confirm password field with "password123!"
    Then an error message "Username is invalid" is displayed

  Scenario: Create an account but an account with the given username already exists (Error Flow)

  I should not be able to create an account if an account with the same username already exists

    Given an account with the username "wash_maclothes" already exists
    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "password123!"
    And I fill in the confirm password field with "password123!"
    Then an error message "Username already exists" is displayed

  Scenario: Create an account but the password field is invalid (Error Flow)

  I should not be able to create an account with an invalid password

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with ""
    And I fill in the confirm password field with ""
    Then an error message "Password is invalid" is displayed

  Scenario: Create an account but the password fields do not match (Error Flow)

  I should not be able to create an account if the password field does not match the confirm password field

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "password123!"
    And I fill in the confirm password field with "password456_"
    Then an error message "Passwords do not match" is displayed

