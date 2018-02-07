package controllers
import play.api.mvc._

case class RequestWithModel[A, B](request: Request[A], model: B) extends WrappedRequest(request)
