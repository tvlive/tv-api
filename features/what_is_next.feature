Feature: What is next on TV

  Background:
    Given the TV Provider "Freeview"
    And today is "15/03/2015"
    And the time is "4:30 am"

  @85
  Scenario: Next content on TV
    Given the TV guide now is:
      | id                       | type    | title                  | start   | end     | rating | imdbId    | episode title | season | episode | Channel   |
      | 55133bc701000001006ab741 | film    | Birdman                | 3:00 am | 6:00 am | 9.4    | 345678941 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab742 | film    | Boyhood                | 6:00 am | 8:00 am | 8.4    | 345678942 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab743 | program | The Common Denominator | 3:00 am | 5:30 am |        |           |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab744 | program | Wolrd news             | 5:30 am | 7:30 am |        |           |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab745 | series  | Friends                | 4:00 am | 4:45 am | 9.3    | 345678943 | Jellyfish     | 4      | 1       | FILM FOUR |
      | 55133bc701000001006ab746 | series  | Dexter                 | 4:45 am | 6:00 am | 8.4    | 345678944 | Day out       | 6      | 1       | FILM FOUR |

    When I GET the resource "/tvcontent/next/freeview"
    Then the HTTP response is "OK"
    And the response is:
    """
    |[
    {
    |  "channel":"FILM FOUR",
    |  "channelImageURL":"http://localhost:9000/FILM_FOUR.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T04:45:00",
    |  "end":"2015-03-15T06:00:00",
    |  "rating":8.4,
    |  "series":{
    |     "serieTitle":"Dexter",
    |     "episode":{
    |        "episodeTitle":"Day out",
    |        "seasonNumber":"6",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://localhost:9000/images/345678944"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab746",
    |  "onTimeNow":false,
    |  "perCentTimeElapsed":null
    |},
    {
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T06:00:00",
    |      "end":"2015-03-15T08:00:00",
    |      "rating":8.4,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Boyhood",
    |         "poster":"http://localhost:9000/images/345678942"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab742",
    |      "onTimeNow":false,
    |      "perCentTimeElapsed":null
    |},
    |{
    |  "channel":"CHANNEL 4",
    |  "channelImageURL":"http://localhost:9000/CHANNEL_4.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T05:30:00",
    |  "end":"2015-03-15T07:30:00",
    |  "rating":null,
    |  "program":{
    |     "title":"Wolrd news"
    |  },
    |  "series":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab744",
    |  "onTimeNow":false,
    |  "perCentTimeElapsed":null
    |}]"""

  @85
  Scenario: No content now on TV
    When I GET the resource "/tvcontent/next/freeview"
    Then the HTTP response is "NOT FOUND"
    And the response is:
    """
    |{
    | "reason": "No next TV content for provider: freeview",
    | "status": 404
    |}"""