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
    And I fill in the password field with "Password123!"
    Then my account should be created with the provided information

  Scenario: Create an account but the full name field is invalid (Error Flow)

  I should not be able to creat an account with an invalid name

    When I fill in the full name field with ""
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "Password123!"
    Then an error message "Account name cannot be empty" is displayed

  Scenario: Create an account but the email is invalid (Error Flow)

  I should not be able to create an account with an invalid email

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "An Invalid Email"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with "Password123!"
    Then an error message "Invalid email address" is displayed

  Scenario: Create an account but the username field is invalid (Error Flow)

  I should not be able to create an account with an invalid username

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with ""
    And I fill in the password field with "Password123!"
    Then an error message "Account username cannot be empty" is displayed

  Scenario: Create an account but an account with the given username already exists (Error Flow)

  I should not be able to create an account if an account with the same username already exists

    Given an account with the username "createAccountUser" already exists
    When I fill in the full name field with "Create Account"
    And I fill in the email field with "testCreateAccount@gmail.com"
    And I fill in the username field with "createAccountUser"
    And I fill in the password field with "TestUser101!"
    Then an error message "Account with username [createAccountUser] exists" is displayed

  Scenario: Create an account but the password field is invalid (Error Flow)

  I should not be able to create an account with an invalid password

    When I fill in the full name field with "Wash MacLothes"
    And I fill in the email field with "wash.maclothes@gmail.com"
    And I fill in the username field with "wash_maclothes"
    And I fill in the password field with ""
    Then an error message "Password cannot be empty" is displayed
