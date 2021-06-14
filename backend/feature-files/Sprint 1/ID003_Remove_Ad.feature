Feature: Remove Ad
  
  As a Customer
  I would like to remove an ad
  So that I can keep washers informed that no service is required anymore

  Background:

    Given I am a Customer using WashIt

  Scenario: Remove an ad successfully (normal flow)

    Given  I have posted an ad that does not have an accepted bid
    When I try to remove the ad 
    Then the ad shall not exist on the system

  Scenario: Remove an ad after request to wash has been accepted (error flow)

    Given  I have posted an ad that does have an accepted bid
    When I try to remove the ad 
    Then the ad shall exist on the system
    Then the system shall notify an error message "Washing process is already in progress. Impossible to delete ad"




