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

    When I GET the resource "/tvcontent/search/freeview?title=Iron+Sky"
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

    When I GET the resource "/tvcontent/search/freeview?title=sun"
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

    When I GET the resource "/tvcontent/search/freeview?title=stories"
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
  Scenario: Search by film title that contains 'gump'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type | title   | start   | end     | rating | poster                    |
      | 55133bc701000001006ab664 | film | Birdman | 3:00 am | 6:00 am | 9.4    | http://images/birdman.jpg |

    When I GET the resource "/tvcontent/search/freeview?title=Birdman"
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
  Scenario: Search by title 'moon' and content type 'program'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type    | title           | start   | end     | rating | poster                            |
      | 55133bc701000001006ab665 | film    | Sky in the moon | 3:00 am | 4:20 am | 9.4    | http://images/sky_in_the_moon.jpg |
      | 55133bc701000001006ab666 | program | Moon walker     | 4:20 am | 5:45 am |        |                                   |

    When I GET the resource "/tvcontent/search/freeview?title=moon&t=program"
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
  Scenario: Search by title 'bridge' and rating '9.4'
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type    | title                 | start   | end     | rating | poster                                  |
      | 55133bc701000001006ab667 | film    | Bridges over the city | 3:00 am | 4:20 am | 9.4    | http://images/bridges_over_the_city.jpg |
      | 55133bc701000001006ab668 | program | Jeff Bridges bio      | 4:20 am | 5:45 am |        |                                         |

    When I GET the resource "/tvcontent/search/freeview?title=bridge&r=9.4"
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
  Scenario: Search TV Content by 'no content exist'
    When I GET the resource "/tvcontent/search/freeview?title=no+content+exist"
    Then the HTTP response is "NOT FOUND"
    And the response is:
    """
    |{
    | "reason": "No TV content found in search by: 'no content exist' and for provider: freeview",
    | "status": 404
    |}"""
