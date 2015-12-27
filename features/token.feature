Feature: Create token

  @99
  Scenario: Create token succesfully
    When I POST the resource '/token' with:
      | username                   | date expired        |
      | alvaro.vilaplana@tvlive.io | 10/10/2016 02:00:00 |
    Then the HTTP status is 'CREATED'
    And the response is:
    """
    {"token":"YWx2YXJvLnZpbGFwbGFuYUB0dmxpdmUuaW86MTIzNDU2Nzg5MDA5ODc2NTQzMjE="}
    """

  @99
  Scenario: Unsuccesful token creation due to no body
    When I POST the resource '/token' with no body:
    Then the HTTP status is 'BAD REQUEST'

  @99
  Scenario Outline: Unsuccesful token creation due to downstream problems
    When I POST the resource '/token' with downstream '<downstream http status>':
      | username                   | date expired        |
      | alvaro.vilaplana@tvlive.io | 10/10/2016 02:00:00 |
    Then the HTTP status is '<downstream http status>'
    Examples:
      | downstream http status |
      | BAD REQUEST            |
      | INTERNAL SERVER ERROR  |
