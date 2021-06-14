Feature: Users can direct message each other

As a user of Wash-It
I would like to direct message another user of Wash-It
So that we can discuss further details related to the laundry service

  Background: 
    Given I am a user of Wash-It
      And I am logged in
      And there is another user of Wash-It I wish to communicate with
  
  Scenario: I successfully send a direct message to another user of Wash-It (Normal Flow):
     When I compose a message
      And I request to send that message to the desired user
     Then the message is added to the list of messages between those two users
      And that user should get a notification that they have a message waiting for them
  
  Scenario: I fail to send a direct message because it contains inappropriate key words (Error Flow):
     When I compose a message that contains any of the flagged key words
      And I request to send that message to the desired user
     Then the message is not added to the list of messages between those two users
      And and error message is displayed indicating that the message was not sent due to inappropriate language
  
