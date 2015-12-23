Feature: Create token

  @token
  Scenario: Create token succesfully
    When I POST the resource '/token' with:
      | username                   | date expired        |
      | alvaro.vilaplana@tvlive.io | 10/10/2016 02:00:00 |
    Then the HTTP status is 'CREATED'
    And the response is:
    """
    {"token":"12345678900987654321"}
    """

  @token
  Scenario: Unsuccesful token creation due to no body
    When I POST the resource '/token' with no body:
    Then the HTTP status is 'BAD REQUEST'

  @token
  Scenario Outline: Unsuccesful token creation due to downstream problems
    When I POST the resource '/token' with downstream '<downstream http status>':
      | username                   | date expired        |
      | alvaro.vilaplana@tvlive.io | 10/10/2016 02:00:00 |
    Then the HTTP status is '<downstream http status>'
    Examples:
      | downstream http status |
      | BAD REQUEST            |
      | INTERNAL SERVER ERROR  |
