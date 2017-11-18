package controllers

import javax.inject.Inject
import play.api.mvc.{Action, Controller}
import services.RollCallService

import scala.concurrent.{ExecutionContext, Future}

class RollCallController @Inject()(rollCallService: RollCallService)(implicit exec: ExecutionContext) extends Controller {

  //HTTP Methode : POST - URL : http://localhost:9000/api/hrms/v1/rollcall/present
  //JSON example : {"employ_id":1}
  def present = Action.async(parse.json) { request => {
    for {
      employ_id <- (request.body \ "employ_id").asOpt[Long]
    } yield {
      rollCallService.present(employ_id).map(Ok(_))
    }
  }.getOrElse(Future {BadRequest(s"""{"ok":"false","message":"Wrong json format"}""")})
  }



//  PUT    /:id/absent                   controllers.RollCallController.absent(id: Long)
//  def absent = Action.async {
//    rollCallService.get(1).map(Ok(_))
//  }

}
