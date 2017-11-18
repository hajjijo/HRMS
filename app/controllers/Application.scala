package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

class Application @Inject()()(implicit ex: ExecutionContext) extends Controller {

  def index = Action {
    Ok("server is up ...")
  }

}