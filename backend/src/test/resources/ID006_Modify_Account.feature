Feature: Modify own Account

  As a user of Wash-It, I would like modify my account
  so that I can update my personal information to my liking

  Background:
    Given I am a user of Wash-It with the following account information:
      |username|email|password|fullName|
      |RegularCustomer|help4laundryy@gmail.com|pAssWord123$$|Bobb Builder|

  Scenario Outline: Modify own account with valid information (normal flow)
    When I provide an "<category>" with valid information "<information>"
    Then my account's information should be updated

    Examples:
      |category|information|
      |email|independant@gmail.com|
      |password|NEwPas123!!|
      |fullName|Markyy Mark|

  Scenario Outline: Modify own account with invalid information (error flow)
    When I provide an "<category>" with invalid information "<information>"
    Then an error message "<message>" is issued

    Examples:
      |category|information|message|
      |email|         |Account email cannot be empty|
      |email|Invalid Email|Invalid email address|
      |password|                         |Password cannot be empty|
      |fullName|                         |Account name cannot be empty|
