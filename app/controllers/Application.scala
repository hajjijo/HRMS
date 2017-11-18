package controllers

import javax.inject.Inject

import models.DateTest
import org.joda.time.DateTime
import play.api.mvc._
import services.DateTestService

import scala.concurrent.{ExecutionContext, Future}

class Application @Inject()(dateTestService: DateTestService)(implicit ex: ExecutionContext) extends Controller {

  def index = Action {
    Ok("server is up ...")
  }

  //{"date":"2107-09-03T10:20"}
  def date = Action.async {
    dateTestService.insert(DateTest(DateTime.now)).map(Ok(_))
  }

  def getDate = Action.async {

    dateTestService.get(1).map(Ok(_))
  }

}