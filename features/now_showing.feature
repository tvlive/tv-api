Feature: What is on TV now

  Background:
    Given the TV Provider "Freeview"
    And today is "15/03/2015"
    And the time is "4:30 am"
    And the user 'william.mark@tvlive.io' with token '0123456789'


  @31 @101
  Scenario: Current content on TV is a film
    Given the TV guide now for channel "BBC ONE" is:
      | id                       | type | title   | start   | end     | rating | imdbId      | posterImdb           |
      | 55133bc701000001006ab641 | film | Birdman | 3:00 am | 6:00 am | 7.8    | 01234567890 | http://imdb/12345678 |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "OK"
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
    |      "rating":7.8,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Birdman",
    |         "poster":"http://localhost:9000/images/01234567890"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab641",
    |      "onTimeNow":true,
    |      "perCentTimeElapsed":50
    |   }]
   """

  @31 @101
  Scenario: Current content on TV is a program
    And the TV guide now for channel "CHANNEL 4" is:
      | id                       | type    | title                  | start   | end     |
      | 55133bc701000001006ab642 | program | The Common Denominator | 3:00 am | 5:30 am |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "OK"
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
    |  "rating":null,
    |  "program":{
    |     "title":"The Common Denominator"
    |  },
    |  "series":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab642",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":60
    |}]"""

  @31 @101
  Scenario: Current content on TV is a series
    And the TV guide now for channel "FILM FOUR" is:
      | id                       | type   | title   | start   | end     | rating | imdbId     | posterImdb           | episode title | season | episode |
      | 55133bc701000001006ab643 | series | Friends | 4:00 am | 4:45 am | 9.3    | 1098765432 | http://imdb/98765432 | Jellyfish     | 4      | 1       |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "OK"
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
    |  "rating":9.3,
    |  "series":{
    |     "serieTitle":"Friends",
    |     "episode":{
    |        "episodeTitle":"Jellyfish",
    |        "seasonNumber":"4",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://localhost:9000/images/1098765432"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab643",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":66
    |}]"""

  @31 @101
  Scenario: Current content on TV
    Given the TV guide now is:
      | id                       | type    | title                  | start   | end     | rating | imdbId    | posterImdb           | episode title | season | episode | Channel   |
      | 55133bc701000001006ab644 | film    | Birdman                | 3:00 am | 6:00 am | 9.4    | 234567891 | http://imdb/34567432 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab645 | program | The Common Denominator | 3:00 am | 5:30 am |        |           |                      |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab646 | series  | Friends                | 4:00 am | 4:45 am | 9.3    | 456789012 | http://imdb/34568765 | Jellyfish     | 4      | 1       | FILM FOUR |
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "OK"
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
    |         "poster":"http://localhost:9000/images/234567891"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab644",
    |      "onTimeNow":true,
    |      "perCentTimeElapsed":50
    |},
    {
    |  "channel":"FILM FOUR",
    |  "channelImageURL":"http://localhost:9000/FILM_FOUR.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:00:00",
    |  "end":"2015-03-15T04:45:00",
    |  "rating":9.3,
    |  "series":{
    |     "serieTitle":"Friends",
    |     "episode":{
    |        "episodeTitle":"Jellyfish",
    |        "seasonNumber":"4",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://localhost:9000/images/456789012"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab646",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":66
    |},
    |{
    |  "channel":"CHANNEL 4",
    |  "channelImageURL":"http://localhost:9000/CHANNEL_4.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T03:00:00",
    |  "end":"2015-03-15T05:30:00",
    |  "rating":null,
    |  "program":{
    |     "title":"The Common Denominator"
    |  },
    |  "series":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab645",
    |  "onTimeNow":true,
    |  "perCentTimeElapsed":60
    |}]"""

  @31 @101
  Scenario: No content now on TV
    When I GET the resource "/tvcontent/all/freeview/current"
    Then the HTTP status is "OK"
    And the response is empty list