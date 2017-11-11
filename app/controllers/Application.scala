package controllers

import play.api.mvc._

class Application extends Controller {

  def index = Action {

    Ok("server is up ...")
  }

}