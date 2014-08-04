import models.{TVChannel, TVChannelRepository, TVContentRepository, TVProgram}
import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

//  val tvChannel1 = TVChannel("testTvChannel1", "EN")
//  val tvChannel2 = TVChannel("testTvChannel2", "EN")
//  val tvChannel3 = TVChannel("testTvChannel3", "EN")
//  val tvChannel4 = TVChannel("testTvChannel4", "EN")
//
//  val tvChannelRepository = TVChannelRepository("tvChannel")
//  val collection = tvChannelRepository.collection
//  collection.drop()
//
//  val tvProgram1 = TVProgram("channel1", "program1", 0, 1, "program_type1", Some(BSONObjectID.generate))
//  val tvProgram2 = TVProgram("channel1", "program2", 1, 2, "program_type2", Some(BSONObjectID.generate))
//  val tvProgram3 = TVProgram("channel1", "program3", 2, 4, "program_type3", Some(BSONObjectID.generate))
//  val tvProgram4 = TVProgram("channel1", "program4", 4, 5, "program_type4", Some(BSONObjectID.generate))
//  val tvProgram5 = TVProgram("channel1", "program5", 5, 12, "program_type5", Some(BSONObjectID.generate))
//
//  val tvContentRepository = TVContentRepository("tvContent")
//  val programCollection = tvContentRepository.collection
//
//  programCollection.drop()
//
//  def before  {
//    insertTvChannel()
//    insertTvProgram()
//  }
//
//  def after  {
////    collection.drop()
////    programCollection.drop()
//  }


  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Your new application is ready.")
    }

    "provide the list of channels available in TV" in new WithApplication {

            val tvChannelRepository = TVChannelRepository("tvChannel")
            val collection = tvChannelRepository.collection
            collection.drop()

            val tvChannel1 = TVChannel("testTvChannel1", "EN")
            val tvChannel2 = TVChannel("testTvChannel2", "EN")
            val tvChannel3 = TVChannel("testTvChannel3", "EN")
            val tvChannel4 = TVChannel("testTvChannel4", "EN")

            val channel1 = collection.insert[TVChannel](tvChannel1)
            val channel2 = collection.insert[TVChannel](tvChannel2)
            val channel3 = collection.insert[TVChannel](tvChannel3)
            val channel4 = collection.insert[TVChannel](tvChannel4)

            val result = for {
              c1 <- channel1
              c2 <- channel2
              c3 <- channel3
              c4 <- channel4
            } yield (c1.ok && c2.ok && c3.ok && c4.ok)

            val isOk = Await.result(result, Duration("20 seconds"))

            isOk match {
              case true => println("Elements inserted")
              case false => {
                collection.drop()
                throw new Exception("Error inserting elements")
              }
            }

      val channels = route(FakeRequest(GET, "/channels")).get
      status(channels) must equalTo(OK)
      contentType(channels) must beSome.which(_ == "application/json")
      println(contentAsString(channels))
      val channelsInResponse = contentAsJson(channels).as[Seq[TVChannel]]
      channelsInResponse must contain(tvChannel1)
      channelsInResponse must contain(tvChannel2)
      channelsInResponse must contain(tvChannel3)
      channelsInResponse must contain(tvChannel4)

    }

    "return all the TV content for a particular channel available today" in new WithApplication {

      val tvContentRepository = TVContentRepository("tvContent")
      val collection = tvContentRepository.collection
      collection.drop()

      val tvProgram1 = TVProgram("channel1", "program1", 0, 1, "program_type1", Some(BSONObjectID.generate))
      val tvProgram2 = TVProgram("channel1", "program2", 1, 2, "program_type2", Some(BSONObjectID.generate))
      val tvProgram3 = TVProgram("channel1", "program3", 2, 4, "program_type3", Some(BSONObjectID.generate))
      val tvProgram4 = TVProgram("channel1", "program4", 4, 5, "program_type4", Some(BSONObjectID.generate))
      val tvProgram5 = TVProgram("channel1", "program5", 5, 12, "program_type5", Some(BSONObjectID.generate))

      val p1 = collection.insert[TVProgram](tvProgram1)
      val p2 = collection.insert[TVProgram](tvProgram2)
      val p3 = collection.insert[TVProgram](tvProgram3)
      val p4 = collection.insert[TVProgram](tvProgram4)
      val p5 = collection.insert[TVProgram](tvProgram5)

      val result = for {
        c1 <- p1
        c2 <- p2
        c3 <- p3
        c4 <- p4
        c5 <- p5
      } yield (c1.ok && c2.ok && c3.ok && c4.ok && c5.ok)

      val isOk = Await.result(result, Duration("20 seconds"))

      isOk match {
        case true => println("Elements inserted")
        case false => {
          collection.drop()
          throw new Exception("Error inserting elements")
        }
      }

      val programs = route(FakeRequest(GET, "/channel/channel1/today")).get
      status(programs) must equalTo(OK)
      contentType(programs) must beSome.which(_ == "application/json")
      println(contentAsString(programs))
      val programsInResponse = contentAsJson(programs).as[Seq[TVProgram]]
      programsInResponse must contain(tvProgram1)
      programsInResponse must contain(tvProgram2)
      programsInResponse must contain(tvProgram3)
      programsInResponse must contain(tvProgram4)
      programsInResponse must contain(tvProgram5)
    }

    "return the TV content for a particular channel available now" in new WithApplication {

      val tvContentRepository = TVContentRepository("tvContent")
      val collection = tvContentRepository.collection
      collection.drop()

      val now = DateTime.now()

      val tvProgram1 = TVProgram("channel1", "program1", now.minusHours(3).toDate.getTime, now.minusHours(2).toDate.getTime, "program_type1", Some(BSONObjectID.generate))
      val tvProgram2 = TVProgram("channel1", "program2", now.minusHours(2).toDate.getTime, now.minusHours(1).toDate.getTime, "program_type2", Some(BSONObjectID.generate))
      val tvProgram3 = TVProgram("channel1", "program3", now.minusHours(1).toDate.getTime, now.plusHours(1).toDate.getTime, "program_type3", Some(BSONObjectID.generate))
      val tvProgram4 = TVProgram("channel1", "program4", now.plusHours(1).toDate.getTime, now.plusHours(2).toDate.getTime, "program_type4", Some(BSONObjectID.generate))
      val tvProgram5 = TVProgram("channel1", "program5", now.plusHours(2).toDate.getTime, now.plusHours(3).toDate.getTime, "program_type5", Some(BSONObjectID.generate))

      val p1 = collection.insert[TVProgram](tvProgram1)
      val p2 = collection.insert[TVProgram](tvProgram2)
      val p3 = collection.insert[TVProgram](tvProgram3)
      val p4 = collection.insert[TVProgram](tvProgram4)
      val p5 = collection.insert[TVProgram](tvProgram5)

      val result = for {
        c1 <- p1
        c2 <- p2
        c3 <- p3
        c4 <- p4
        c5 <- p5
      } yield (c1.ok && c2.ok && c3.ok && c4.ok && c5.ok)

      val isOk = Await.result(result, Duration("20 seconds"))

      isOk match {
        case true => println("Elements inserted")
        case false => {
          collection.drop()
          throw new Exception("Error inserting elements")
        }
      }

      val programs = route(FakeRequest(GET, "/channel/channel1/current")).get
      status(programs) must equalTo(OK)
      contentType(programs) must beSome.which(_ == "application/json")
      println(contentAsString(programs))
      val programsInResponse = contentAsJson(programs).as[TVProgram]
      programsInResponse must be_==(tvProgram3)
    }

    "return the TV content for a particular channel available from now until the end of the day" in new WithApplication {

      val tvContentRepository = TVContentRepository("tvContent")
      val collection = tvContentRepository.collection
      collection.drop()

      val now = DateTime.now()

      val tvProgram1 = TVProgram("channel1", "program1", now.minusHours(3).toDate.getTime, now.minusHours(2).toDate.getTime, "program_type1", Some(BSONObjectID.generate))
      val tvProgram2 = TVProgram("channel1", "program2", now.minusHours(2).toDate.getTime, now.minusHours(1).toDate.getTime, "program_type2", Some(BSONObjectID.generate))
      val tvProgram3 = TVProgram("channel1", "program3", now.minusHours(1).toDate.getTime, now.plusHours(1).toDate.getTime, "program_type3", Some(BSONObjectID.generate))
      val tvProgram4 = TVProgram("channel1", "program4", now.plusHours(1).toDate.getTime, now.plusHours(2).toDate.getTime, "program_type4", Some(BSONObjectID.generate))
      val tvProgram5 = TVProgram("channel1", "program5", now.plusHours(2).toDate.getTime, now.plusHours(3).toDate.getTime, "program_type5", Some(BSONObjectID.generate))

      val p1 = collection.insert[TVProgram](tvProgram1)
      val p2 = collection.insert[TVProgram](tvProgram2)
      val p3 = collection.insert[TVProgram](tvProgram3)
      val p4 = collection.insert[TVProgram](tvProgram4)
      val p5 = collection.insert[TVProgram](tvProgram5)

      val result = for {
        c1 <- p1
        c2 <- p2
        c3 <- p3
        c4 <- p4
        c5 <- p5
      } yield (c1.ok && c2.ok && c3.ok && c4.ok && c5.ok)

      val isOk = Await.result(result, Duration("20 seconds"))

      isOk match {
        case true => println("Elements inserted")
        case false => {
          collection.drop()
          throw new Exception("Error inserting elements")
        }
      }

      val programs = route(FakeRequest(GET, "/channel/channel1/left")).get
      status(programs) must equalTo(OK)
      contentType(programs) must beSome.which(_ == "application/json")
      println(contentAsString(programs))
      val programsInResponse = contentAsJson(programs).as[Seq[TVProgram]]
      programsInResponse must contain(tvProgram3)
      programsInResponse must contain(tvProgram4)
      programsInResponse must contain(tvProgram5)
    }
  }

//  private def insertTvProgram() {
//    val p1 = programCollection.insert[TVProgram](tvProgram1)
//    val p2 = programCollection.insert[TVProgram](tvProgram2)
//    val p3 = programCollection.insert[TVProgram](tvProgram3)
//    val p4 = programCollection.insert[TVProgram](tvProgram4)
//    val p5 = programCollection.insert[TVProgram](tvProgram5)
//
//    val result = for {
//      c1 <- p1
//      c2 <- p2
//      c3 <- p3
//      c4 <- p4
//      c5 <- p5
//    } yield (c1.ok && c2.ok && c3.ok && c4.ok && c5.ok)
//
//    val isOk = Await.result(result, Duration("20 seconds"))
//
//    isOk match {
//      case true => println("Elements inserted")
//      case false => {
//        collection.drop()
//        throw new Exception("Error inserting elements")
//      }
//    }
//
//  }
//
//  private def insertTvChannel() {
//    val channel1 = collection.insert[TVChannel](tvChannel1)
//    val channel2 = collection.insert[TVChannel](tvChannel2)
//    val channel3 = collection.insert[TVChannel](tvChannel3)
//    val channel4 = collection.insert[TVChannel](tvChannel4)
//
//    val result = for {
//      c1 <- channel1
//      c2 <- channel2
//      c3 <- channel3
//      c4 <- channel4
//    } yield (c1.ok && c2.ok && c3.ok && c4.ok)
//
//    val isOk = Await.result(result, Duration("20 seconds"))
//
//    isOk match {
//      case true => println("Elements inserted")
//      case false => {
//        collection.drop()
//        throw new Exception("Error inserting elements")
//      }
//    }
//
//  }
}
