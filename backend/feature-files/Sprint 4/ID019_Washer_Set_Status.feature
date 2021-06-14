Feature: Washer can set their status

As a User of Wash-It who is a Washer
I would like set myself as Away or Online
so that other users know when it is appropriate to contact me

  Background:
    Given I am a user of Wash-It
      And I am logged in

  Scenario: I successfully set myself as Away (Normal Flow):
    Given my status was Online
     When I set my status to Away
     Then my status changes to Away

  Scenario: I successfully set myself as Online (Alternate Flow):
    Given my status was Away
     When I set my status to Online
     Then my status changes to Online
