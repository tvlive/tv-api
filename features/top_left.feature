Feature: Top content on TV from now to the end of the day

  Background:
    Given the TV Provider "Freeview"
    And today is "15/03/2015"
    And the time is "4:30 am"
    And the user 'william.mark@tvlive.io' with token '0123456789'

  @77 @101
  Scenario: 2 top contents left
    Given the TV guide for the rest of the day is:
      | id                       | type   | title   | start   | end     | rating | imdbId     | posterImdb            | episode title | season | episode | Channel   |
      | 55133bc701000001006ab631 | film   | Birdman | 5:00 am | 6:45 am | 7.8    | 1023456781 | http://imdb/678900980 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab632 | film   | Boyhood | 4:55 am | 6:00 am | 8.3    | 1023456782 | http://imdb/678900981 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab633 | series | Friends | 5:00 am | 5:45 am | 8.3    | 1023456784 | http://imdb/678900982 | Jellyfish     | 4      | 1       | FILM FOUR |
      | 55133bc701000001006ab634 | series | Dexter  | 5:45 am | 6:45 am | 9.0    | 1023456783 | http://imdb/678900983 | My bad        | 5      | 1       | FILM FOUR |
    When I GET the resource "/tvcontent/top/freeview?items=2"
    Then the HTTP status is "OK"
    And the response is:
    """
    |[{
    |  "channel":"FILM FOUR",
    |  "channelImageURL":"http://localhost:9000/FILM_FOUR.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T05:45:00",
    |  "end":"2015-03-15T06:45:00",
    |  "rating":9.0,
    |  "series":{
    |     "serieTitle":"Dexter",
    |     "episode":{
    |        "episodeTitle":"My bad",
    |        "seasonNumber":"5",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://localhost:9000/images/1023456783"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab634",
    |  "onTimeNow":false,
    |  "perCentTimeElapsed":null
    |},
    |{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T04:55:00",
    |      "end":"2015-03-15T06:00:00",
    |      "rating":8.3,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Boyhood",
    |         "poster":"http://localhost:9000/images/1023456782"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab632",
    |      "onTimeNow":false,
    |      "perCentTimeElapsed":null
    |   }]"""


  @77 @101
  Scenario: 3 top contents left
    Given the TV guide for the rest of the day is:
      | id                       | type    | title                  | start   | end     | rating | imdbId    | posterImdb            | episode title | season | episode | Channel   |
      | 55133bc701000001006ab635 | program | BBC News               | 5:00 am | 6:45 am |        |           |                       |               |        |         | BBC ONE   |
      | 55133bc701000001006ab636 | film    | Boyhood                | 6:45 am | 8:00 am | 8.3    | 345678941 | http://imdb/678900987 |               |        |         | BBC ONE   |
      | 55133bc701000001006ab637 | series  | Friends                | 5:00 am | 5:45 am | 8.3    | 345678942 | http://imdb/678900988 | Jellyfish     | 4      | 1       | FILM FOUR |
      | 55133bc701000001006ab638 | program | Weather                | 5:45 am | 6:45 am |        |           |                       |               |        |         | FILM FOUR |
      | 55133bc701000001006ab639 | program | Party Election         | 2:50 am | 5:30 am |        |           |                       |               |        |         | CHANNEL 4 |
      | 55133bc701000001006ab640 | program | The Common Denominator | 3:00 am | 5:30 am |        |           |                       |               |        |         | CHANNEL 4 |

    When I GET the resource "/tvcontent/top/freeview?items=3"
    Then the HTTP status is "OK"
    And the response is:
    """
    |[{
    |  "channel":"FILM FOUR",
    |  "channelImageURL":"http://localhost:9000/FILM_FOUR.png",
    |  "provider":[
    |     "FREEVIEW"
    |  ],
    |  "start":"2015-03-15T05:00:00",
    |  "end":"2015-03-15T05:45:00",
    |  "rating":8.3,
    |  "series":{
    |     "serieTitle":"Friends",
    |     "episode":{
    |        "episodeTitle":"Jellyfish",
    |        "seasonNumber":"4",
    |        "episodeNumber":"1"
    |     },
    |     "poster":"http://localhost:9000/images/345678942"
    |  },
    |  "program":null,
    |  "film":null,
    |  "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab637",
    |  "onTimeNow":false,
    |  "perCentTimeElapsed":null
    |},
    |{
    |      "channel":"BBC ONE",
    |      "channelImageURL":"http://localhost:9000/BBC_ONE.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T06:45:00",
    |      "end":"2015-03-15T08:00:00",
    |      "rating":8.3,
    |      "program": null,
    |      "series": null,
    |      "film":{
    |         "title":"Boyhood",
    |         "poster":"http://localhost:9000/images/345678941"
    |      },
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab636",
    |      "onTimeNow":false,
    |      "perCentTimeElapsed":null
    |},
    |{
    |      "channel":"CHANNEL 4",
    |      "channelImageURL":"http://localhost:9000/CHANNEL_4.png",
    |      "provider":[
    |         "FREEVIEW"
    |      ],
    |      "start":"2015-03-15T02:50:00",
    |      "end":"2015-03-15T05:30:00",
    |      "rating": null,
    |      "program":{
    |       "title":"Party Election"
    |      },
    |      "series": null,
    |      "film": null,
    |      "uriTVContentDetails":"http://localhost:9000/tvcontent/55133bc701000001006ab639",
    |      "onTimeNow":true,
    |      "perCentTimeElapsed":62
    |   }]"""

  @77 @101
  Scenario: No content now on TV
    When I GET the resource "/tvcontent/top/freeview?items=3"
    Then the HTTP status is "OK"
    And the response is empty list
