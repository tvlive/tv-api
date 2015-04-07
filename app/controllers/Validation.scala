package controllers

import play.api.libs.json.Json
import play.api.mvc.Results

import scala.concurrent.Future


trait Validation extends Results {

  import play.api.mvc._

  private def isValidContent(c: String) = c.toLowerCase == "series" || c.toLowerCase == "film" || c.toLowerCase == "program"

  class SearchRequest[A](val title: Option[String],
                         val rating: Option[Double],
                         val content: Option[String],
                         request: Request[A]) extends WrappedRequest[A](request)

  def searchValidation(title: Option[String], rating: Option[Double], content: Option[String]) = new ActionBuilder[SearchRequest] {
    def invokeBlock[A](request: Request[A], block: (SearchRequest[A]) => Future[SimpleResult]) = {

      (title.isDefined || rating.isDefined, content) match {
        case (true, Some(c)) if isValidContent(c) => block(new SearchRequest(title, rating, content, request))
        case (true, None) => block(new SearchRequest(title, rating, content, request))
        case (_, _) => Future.successful(BadRequest(Json.toJson(BadRequestResponse(s"Search parameters not valid"))))
      }
    }
  }

  class ContentRequest[A](val content: String, request: Request[A]) extends WrappedRequest[A](request)

  def tvcontentValidation(content: String) = new ActionBuilder[ContentRequest] {
    def invokeBlock[A](request: Request[A], block: (ContentRequest[A]) => Future[SimpleResult]) = {

      if (isValidContent(content)) block(new ContentRequest(content, request))
      else Future.successful(BadRequest(Json.toJson(BadRequestResponse(s"TV content $content does not exist"))))
    }
  }
}
