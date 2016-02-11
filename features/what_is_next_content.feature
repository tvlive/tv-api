Feature: What is next on TV

  Background:
    Given the TV Provider "Freeview"
    And today is "15/03/2015"
    And the time is "4:30 am"
    And the user 'william.mark@tvlive.io' with token '0123456789'

  @103
  Scenario: Next films on TV
    Given the TV guide now is:
      | id                       | type    | title                  | start   | end      | rating | imdbId    | posterImdb            | episode title | season | episode | Channel   |
      | 55133bc701000001006ab741 | film    | Birdman                | 3:00 am | 6:00 am  | 9.4    | 345678941 | http://imdb/678900970 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab742 | film    | Boyhood                | 6:00 am | 8:00 am  | 8.4    | 345678942 | http://imdb/678900962 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab743 | film    | Spotlight              | 8:00 am | 10:00 am | 6.4    | 345678943 | http://imdb/678900963 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab744 | program | The Common Denominator | 3:00 am | 5:30 am  |        |           |                       |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab745 | program | Wolrd news             | 5:30 am | 7:30 am  |        |           |                       |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab746 | film    | Superman               | 7:30 am | 9:00 am  | 9.4    | 345678944 | http://imdb/678900964 |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab747 | series  | Friends                | 4:00 am | 4:45 am  | 9.3    | 345678945 | http://imdb/678900951 | Jellyfish     | 4      | 1       | FILM FOUR |
      | 55133bc701000001006ab748 | film    | Star wars              | 4:45 am | 8:00 am  | 7.0    | 345678944 | http://imdb/678900965 |               |        |         | FILM FOUR |

    When I GET the resource "/tvcontent/film/freeview/next"
    Then the HTTP status is "OK"
    And the response is:
    """
    |[
    {
    |      "channel":"CHANNEL 4",
    |      "channelImageURL":"http://localhost:9000/CHANNEL_4.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T07:30:00",
    |      "end":"2015-03-15T09:00:00",
    |      "rating":9.4,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Superman",
    |         "poster":"http://localhost:9000/images/345678944"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab746",
    |      "onTimeNow":false,
    |      "perCentTimeElapsed":null
    |},
    |{
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
    |},{
    |      "channel":"FILM FOUR",
    |      "channelImageURL":"http://localhost:9000/FILM_FOUR.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T04:45:00",
    |      "end":"2015-03-15T08:00:00",
    |      "rating":7.0,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Star wars",
    |         "poster":"http://localhost:9000/images/345678944"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab748",
    |      "onTimeNow":false,
    |      "perCentTimeElapsed":null
    |}]"""

  @103
  Scenario: Next series on TV
    Given the TV guide now is:
      | id                       | type    | title                  | start   | end     | rating | imdbId    | posterImdb            | episode title | season | episode | Channel   |
      | 55133bc701000001006ab741 | film    | Birdman                | 3:00 am | 6:00 am | 9.4    | 345678941 | http://imdb/678900970 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab742 | series  | Friends                | 6:00 am | 6:45 am | 8.3    | 345678945 | http://imdb/678900951 | Jellyfish     | 4      | 1       | BBC ONE   |
      | 55133bc701000001006ab743 | series  | Big Bang Theroy        | 6:45 am | 7:45 am | 6.3    | 345678945 | http://imdb/678900951 | World         | 5      | 1       | BBC ONE   |
      | 55133bc701000001006ab744 | program | The Common Denominator | 3:00 am | 5:30 am |        |           |                       |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab745 | program | Wolrd news             | 5:30 am | 7:30 am |        |           |                       |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab746 | series  | Fraser                 | 7:30 am | 8:45 am | 9.7    | 345678945 | http://imdb/678900951 | Crashing love | 6      | 1       | CHANNEL 4 |
      | 55133bc701000001006ab747 | series  | Friends                | 4:00 am | 4:45 am | 9.3    | 345678945 | http://imdb/678900951 | My brother    | 7      | 1       | FILM FOUR |
      | 55133bc701000001006ab748 | film    | Star wars              | 4:45 am | 8:00 am | 7.0    | 345678944 | http://imdb/678900965 |               |        |         | FILM FOUR |

    When I GET the resource "/tvcontent/series/freeview/next"
    Then the HTTP status is "OK"
    And the response is:
    """
    |[
    {
    |  "channel":"CHANNEL 4",
    |  "channelImageURL":"http://localhost:9000/CHANNEL_4.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T07:30:00",
    |  "end":"2015-03-15T08:45:00",
    |  "rating":9.7,
    |  "series":{
    |     "serieTitle":"Fraser",
    |     "episode":{
    |        "episodeTitle":"Crashing love",
    |        "seasonNumber":"6",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://localhost:9000/images/345678945"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab746",
    |  "onTimeNow":false,
    |  "perCentTimeElapsed":null
    |},
    {
    |  "channel":"BBC ONE",
    |  "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T06:00:00",
    |  "end":"2015-03-15T06:45:00",
    |  "rating":8.3,
    |  "series":{
    |     "serieTitle":"Friends",
    |     "episode":{
    |        "episodeTitle":"Jellyfish",
    |        "seasonNumber":"4",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://localhost:9000/images/345678945"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab742",
    |  "onTimeNow":false,
    |  "perCentTimeElapsed":null
    |}]"""

  @103
  Scenario: No films next on TV
    When I GET the resource "/tvcontent/film/freeview/next"
    Then the HTTP status is "NOT FOUND"
    And the response is:
    """
    |{
    | "reason": "No next TV content for provider: freeview and content: film",
    | "status": 404
    |}"""