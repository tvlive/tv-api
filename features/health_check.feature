Feature: Health check

  @102
  Scenario: Ping the health check
    When I GET the resource "/ping"
    Then the HTTP status is 'OK'