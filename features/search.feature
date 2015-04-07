Feature: Search TV content by title

  Background:
    Given the TV Provider "Freeview"
    And today is "15/03/2015"
    And the time is "4:30 am"

  @86
  Scenario: Search by series title that contains 'sky'
    Given the TV guide now for channel "BBC TWO" is:
      | id                       | type   | title    | start   | end     | rating | poster                     | episode title | season | episode |
      | 55133bc701000001006ab661 | series | Iron Sky | 4:30 am | 6:45 am | 9.0    | http://images/iron_sky.jpg | Sun in son    | 2      | 1       |

    When I GET the resource "/tvcontent/search/freeview?t=Iron+Sky"
    Then the HTTP response is "OK"
    And the response is:
    """
    | [{
    |  "channel":"BBC TWO",
    |  "channelImageURL":"http://localhost:9000/BBC_TWO.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:30:00",
    |  "end":"2015-03-15T06:45:00",
    |  "rating":9.0,
    |  "series":{
    |     "serieTitle":"Iron Sky",
    |     "episode":{
    |        "episodeTitle":"Sun in son",
    |        "seasonNumber":"2",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://images/iron_sky.jpg"
    |   },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab661",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":0
    |}]"""


  @86
  Scenario: Search by episode title that contains 'sun'
    Given the TV guide now for channel "BBC TWO" is:
      | id                       | type   | title   | start   | end     | rating | poster                     | episode title | season | episode |
      | 55133bc701000001006ab662 | series | Friends | 4:30 am | 6:45 am | 9.0    | http://images/iron_sky.jpg | Sun in son    | 2      | 1       |

    When I GET the resource "/tvcontent/search/freeview?t=sun"
    Then the HTTP response is "OK"
    And the response is:
    """
    | [{
    |  "channel":"BBC TWO",
    |  "channelImageURL":"http://localhost:9000/BBC_TWO.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:30:00",
    |  "end":"2015-03-15T06:45:00",
    |  "rating":9.0,
    |  "series":{
    |     "serieTitle":"Friends",
    |     "episode":{
    |        "episodeTitle":"Sun in son",
    |        "seasonNumber":"2",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://images/iron_sky.jpg"
    |   },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab662",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":0
    |}]"""


  @86
  Scenario: Search by program title contains 'stories'
    Given the TV guide now for channel "CHANNEL FOUR" is:
      | id                       | type    | title       | start   | end     |
      | 55133bc701000001006ab663 | program | Tom stories | 4:20 am | 5:45 am |

    When I GET the resource "/tvcontent/search/freeview?t=stories"
    Then the HTTP response is "OK"
    And the response is:
    """
    | [{
    |  "channel":"CHANNEL FOUR",
    |  "channelImageURL":"http://localhost:9000/CHANNEL_FOUR.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:20:00",
    |  "end":"2015-03-15T05:45:00",
    |  "rating":null,
    |  "program":{
    |     "title":"Tom stories"
    |  },
    |  "series":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab663",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":11
    |}]"""

  @86
  Scenario: Search by film title that contains 'Birdman'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type | title   | start   | end     | rating | poster                    |
      | 55133bc701000001006ab664 | film | Birdman | 3:00 am | 6:00 am | 9.4    | http://images/birdman.jpg |

    When I GET the resource "/tvcontent/search/freeview?t=Birdman"
    Then the HTTP response is "OK"
    And the response is:
    """
    |[{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T03:00:00",
    |      "end":"2015-03-15T06:00:00",
    |      "rating":9.4,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Birdman",
    |         "poster":"http://images/birdman.jpg"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab664",
    |      "onTimeNow":true,
    |      "perCentTimeElapsed":50
    |}]"""


  @86
  Scenario: Search by title 'moon' and TV content type 'program'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type    | title           | start   | end     | rating | poster                            |
      | 55133bc701000001006ab665 | film    | Sky in the moon | 3:00 am | 4:20 am | 9.4    | http://images/sky_in_the_moon.jpg |
      | 55133bc701000001006ab666 | program | Moon walker     | 4:20 am | 5:45 am |        |                                   |

    When I GET the resource "/tvcontent/search/freeview?t=moon&c=program"
    Then the HTTP response is "OK"
    And the response is:
    """
    | [{
    |  "channel":"BBC ONE",
    |  "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:20:00",
    |  "end":"2015-03-15T05:45:00",
    |  "rating":null,
    |  "program":{
    |     "title":"Moon walker"
    |  },
    |  "series":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab666",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":11
    |}]"""

  @86
  Scenario: Search by rating '9.4' and TV content type 'film'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type    | title       | start   | end     | rating | poster                    |
      | 55133bc701000001006ab672 | film    | X and Y     | 3:00 am | 4:20 am | 9.4    | http://images/x_and_y.jpg |
      | 55133bc701000001006ab673 | program | Moon walker | 4:20 am | 5:45 am |        |                           |

    When I GET the resource "/tvcontent/search/freeview?r=9.4&c=film"
    Then the HTTP response is "OK"
    And the response is:
    """
    |[{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T03:00:00",
    |      "end":"2015-03-15T04:20:00",
    |      "rating":9.4,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"X and Y",
    |         "poster":"http://images/x_and_y.jpg"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab672",
    |      "onTimeNow":false,
    |      "perCentTimeElapsed":null
    |}]"""



  @86
  Scenario: Search by title 'bridge' and rating '9.4'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type    | title                 | start   | end     | rating | poster                                  |
      | 55133bc701000001006ab667 | film    | Bridges over the city | 3:00 am | 4:20 am | 9.4    | http://images/bridges_over_the_city.jpg |
      | 55133bc701000001006ab668 | program | Jeff Bridges bio      | 4:20 am | 5:45 am |        |                                         |

    When I GET the resource "/tvcontent/search/freeview?t=bridge&r=9.4"
    Then the HTTP response is "OK"
    And the response is:
    """
    |[{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T03:00:00",
    |      "end":"2015-03-15T04:20:00",
    |      "rating":9.4,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Bridges over the city",
    |         "poster":"http://images/bridges_over_the_city.jpg"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab667",
    |      "onTimeNow":false,
    |      "perCentTimeElapsed":null
    |}]"""

  @86
  Scenario: Search by rating '7.4'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type | title   | start   | end     | rating | poster                                  |
      | 55133bc701000001006ab668 | film | X and Y | 4:20 am | 5:45 am | 7.4    | http://images/x_and_y.jpg |

    When I GET the resource "/tvcontent/search/freeview?r=7.4"
    Then the HTTP response is "OK"
    And the response is:
    """
    |[{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T04:20:00",
    |      "end":"2015-03-15T05:45:00",
    |      "rating":7.4,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"X and Y",
    |         "poster":"http://images/x_and_y.jpg"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab668",
    |      "onTimeNow":true,
    |      "perCentTimeElapsed":11
    |}]"""


  @86
  Scenario: Search by title 'news'
    Given the TV guide for the rest of the day is:
      | id                       | type    | title          | start   | end     | rating | poster                         | episode title | season | episode | Channel   |
      | 55133bc701000001006ab669 | program | BBC News       | 3:00 am | 4:20 am |        |                                |               |        |         | BBC ONE   |
      | 55133bc701000001006ab670 | series  | Weather in NYC | 5:00 am | 5:45 am | 8.3    | http://images/friends.jpg      | Good news     | 4      | 1       | FILM FOUR |
      | 55133bc701000001006ab671 | film    | Hope news      | 3:00 am | 5:00 am | 8.3    | opehttp://images/hope_news.jpg |               |        |         | CHANNEL 4 |
    When I GET the resource "/tvcontent/search/freeview?t=news"
    Then the search contains the next content:
      | type    | title          |
      | film    | Hope news      |
      | series  | Weather in NYC |
      | program | BBC News       |

  @86
  Scenario: Search TV Content with no critera
    When I GET the resource "/tvcontent/search/freeview"
    Then the HTTP response is "BAD REQUEST"
    And the response is:
    """
    |{
    | "reason": "Search parameters not valid",
    | "status": 400
    |}"""

  @86
  Scenario: Search TV Content with right title and wrong tv TV content type
    When I GET the resource "/tvcontent/search/freeview?t=news&c=wrong-tv-content"
    Then the HTTP response is "BAD REQUEST"
    And the response is:
    """
    |{
    | "reason": "Search parameters not valid",
    | "status": 400
    |}"""

  @86
  Scenario: Search TV Content with right rating and wrong tv TV content type
    When I GET the resource "/tvcontent/search/freeview?r=9.3&c=wrong-tv-content"
    Then the HTTP response is "BAD REQUEST"
    And the response is:
    """
    |{
    | "reason": "Search parameters not valid",
    | "status": 400
    |}"""


  @86
  Scenario: Search TV Content by 'no content exist'
    When I GET the resource "/tvcontent/search/freeview?t=no+content+exist"
    Then the HTTP response is "NOT FOUND"
    And the response is:
    """
    |{
    | "reason": "No TV content found in search for provider: freeview",
    | "status": 404
    |}"""
