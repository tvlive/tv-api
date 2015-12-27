Feature: Authorization

  @101
  Scenario: Failed authorization when no Authorization header provided
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "UNAUTHORIZED"

  @101
  Scenario: Failed authorization when credentials does not exist
    When the user 'w.m@tvlive.io' with token '0123456789' does not exist
    And I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "UNAUTHORIZED"


  @101
  Scenario: Failed authorization when encoded token is bad formed
    When the encoded token '0123456789'
    And I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "UNAUTHORIZED"