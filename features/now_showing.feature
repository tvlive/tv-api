Feature: What is on TV now

  Background:
    Given the TV Provider "Freeview"
    And today is "15/03/2015"

  @31
  Scenario: Current content on TV is a film
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type  | title   | start   | end     | rating | poster                    |
      | 55133bc701000001006ab641 | film  | Birdman | 3:00 am | 6:00 am | 7.8    | http://images/birdman.jpg |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP response is "OK"
    And the response is:
    """
    | [{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T03:00:00",
    |      "end":"2015-03-15T06:00:00",
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Birdman",
    |         "rating":7.8,
    |         "poster":"http://images/birdman.jpg"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab641",
    |      "onTimeNow":true,
    |      "perCentTimeElapsed":50
    |   }]
   """

  @31
  Scenario: Current content on TV is a program
    And the TV guide now for channel "CHANNEL 4" is:
      | id                       | type    | title                  | start   | end     |
      | 55133bc701000001006ab642 | program | The Common Denominator | 3:00 am | 5:30 am |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP response is "OK"
    And the response is:
    """
    |[{
    |  "channel":"CHANNEL 4",
    |  "channelImageURL":"http://localhost:9000/CHANNEL_4.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T03:00:00",
    |  "end":"2015-03-15T05:30:00",
    |  "program":{
    |     "title":"The Common Denominator"
    |  },
    |  "series":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab642",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":60
    |}]"""

  @31
  Scenario: Current content on TV is a series
    And the TV guide now for channel "FILM FOUR" is:
      | id                       | type   | title   | start   | end     | rating | poster                    | episode title | season | episode |
      | 55133bc701000001006ab643 | series | Friends | 4:00 am | 4:45 am | 9.3    | http://images/friends.jpg | Jellyfish     | 4      | 1       |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP response is "OK"
    And the response is:
    """
    | [{
    |  "channel":"FILM FOUR",
    |  "channelImageURL":"http://localhost:9000/FILM_FOUR.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:00:00",
    |  "end":"2015-03-15T04:45:00",
    |  "series":{
    |     "serieTitle":"Friends",
    |     "episode":{
    |        "episodeTitle":"Jellyfish",
    |        "seasonNumber":"4",
    |        "episodeNumber":"1"
    |     },
    |     "rating":9.3,
    |     "poster":"http://images/friends.jpg"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab643",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":66
    |}]"""

  @31
  Scenario: Current content on TV
    Given the TV guide now is:
      | id                       | type    | title                  | start   | end     | rating | poster                    | episode title | season | episode | Channel   |
      | 55133bc701000001006ab644 | film    | Birdman                | 3:00 am | 6:00 am | 7.8    | http://images/birdman.jpg |               |        |         | BBC ONE   |
      | 55133bc701000001006ab645 | program | The Common Denominator | 3:00 am | 5:30 am |        |                           |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab646 | series  | Friends                | 4:00 am | 4:45 am | 9.3    | http://images/friends.jpg | Jellyfish     | 4      | 1       | FILM FOUR |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP response is "OK"
    And the response is:
    """
    |[{
    |  "channel":"FILM FOUR",
    |  "channelImageURL":"http://localhost:9000/FILM_FOUR.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:00:00",
    |  "end":"2015-03-15T04:45:00",
    |  "series":{
    |     "serieTitle":"Friends",
    |     "episode":{
    |        "episodeTitle":"Jellyfish",
    |        "seasonNumber":"4",
    |        "episodeNumber":"1"
    |     },
    |     "rating":9.3,
    |     "poster":"http://images/friends.jpg"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab646",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":66
    |},
    |{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T03:00:00",
    |      "end":"2015-03-15T06:00:00",
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Birdman",
    |         "rating":7.8,
    |         "poster":"http://images/birdman.jpg"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab644",
    |      "onTimeNow":true,
    |      "perCentTimeElapsed":50
    |   },
    |{
    |  "channel":"CHANNEL 4",
    |  "channelImageURL":"http://localhost:9000/CHANNEL_4.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T03:00:00",
    |  "end":"2015-03-15T05:30:00",
    |  "program":{
    |     "title":"The Common Denominator"
    |  },
    |  "series":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab645",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":60
    |}]"""

  @31
  Scenario: No content now on TV
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP response is "NOT FOUND"
    And the response is:
    """
    |{
    | "reason": "No TV content at this moment for provider: freeview",
    | "status": 404
    |}"""