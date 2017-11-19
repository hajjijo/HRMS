package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.ExecutionContext

class Application @Inject()()(implicit ex: ExecutionContext) extends Controller {

  def index = Action {
    Ok("server is up ...")
  }

}