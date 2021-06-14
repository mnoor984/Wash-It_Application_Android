Feature: Modify own Account

  As a user of Wash-It, I would like modify my account
  so that I can update my personal information to my liking

  Background:
    Given I am a user of Wash-It
    And my account has the following information:
      | username         | email                   | password | fullname     |
      | RegularCustomer! | help4laundryy@gmail.com | password | Bobb Builder |

    And another user of Wash-It has the following account information:
      | username       | email                   | password | fullname     |
      | BetterCustomer | godwasher101!@gmail.com | PaSsWoRd | Geniuss John |

  Scenario Outline: Modify own account with valid information (normal flow)
    When I provide an "<category>" with valid information "<information>"
    Then my account's "<category>" should have the following "<information>"

    Examples:
      | category | information            |
      | username | OrdinaryyCustomer      |
      | email    | independant!@gmail.com |
      | password | strongPassword         |
      | fullname | Markyy Mark            |

  Scenario Outline: Modify own account with invalid information (error flow)
    When I provide an "<category>" with invalid information "<information>"
    Then an error message "<message>" is issued

    Examples:
      | category | information             | message                                   |
      | username |                         | Username is invalid                       |
      | username | BetterCustomer          | Username already exists                   |
      | email    | Invalid Email           | Email is invalid                          |
      | email    | godwasher101!@gmail.com | An account with that email already exists |
      | password |                         | Password is invalid                       |
      | fullname |                         | Please enter a valid name                 |
