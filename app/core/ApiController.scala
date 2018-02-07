package core

import java.io.{PrintWriter, StringWriter}
import controllers.RequestWithModel
import core.utils.SystemMessages
import play.api.data.validation.ValidationError
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.{JsPath, JsValue, Json, Writes}
import play.api.mvc._
import scala.concurrent.Future
import scala.reflect.ClassTag

trait ApiController extends Controller with I18nSupport with WithExecutionContext {

  def restAction[A: ClassTag, B: ClassTag](inputJsonHandler: JsonFormatter[A], outputJsonHandler: JsonFormatter[B])(block: RequestWithModel[JsValue, A] => Future[RestResult]) = {
    Action.async(parse.json) { implicit request =>
      processRequest(inputJsonHandler, outputJsonHandler, request) { input =>
        withValidationCheck(input) {
          block(RequestWithModel(request, input))
        }
      }
    }
  }

  def restAction[A: ClassTag](outputJsonHandler: JsonFormatter[A])(block: Request[AnyContent] => Future[RestResult]) = {
    Action.async { implicit request =>
      processRequest(outputJsonHandler, request) {
        block(request)
      }
    }
  }

  private def processRequest[A, B: ClassTag](inputJsonHandler: JsonFormatter[A], outputJsonHandler: JsonFormatter[B], request: Request[JsValue])(block: A => Future[RestResult]): Future[Result] = {
    implicit val inputFormat = inputJsonHandler.formatter
    implicit val outputFormat = outputJsonHandler.formatter

    request.body.validate[A].fold(
      invalid => {
        val errorJson = renderInvalidJsonMessages(invalid)
        Future.successful(BadRequest(errorJson))
      },
      input => {
        val outputFuture = block(input)
        outputFuture.onFailure { case ex =>
          val buf = new StringWriter()
          val pbuf = new PrintWriter(buf)
          ex.printStackTrace(pbuf)
          pbuf.flush()
          pbuf.close()
      }
        processResult(outputFuture) map { case (status, jsValue) =>
          status(jsValue)
        }
      }
    )
  }

  private def processRequest[A: ClassTag](outputJsonHandler: JsonFormatter[A], request: Request[_])(block: => Future[RestResult]): Future[Result] = {
    implicit val outputFormat = outputJsonHandler.formatter

    // Call block
    val outputFuture = block
    val processedOutputFuture = processResult(outputFuture) map { case (status, jsValue) =>
      status(jsValue)
    }
    processedOutputFuture
  }

  private def withValidationCheck[A: ClassTag](model: A)(block: => Future[RestResult])(implicit messages: Messages): Future[RestResult] = {
    model match {
      case validatable: Validatable =>
        validatable.getValidationErrors match {
          case Nil => block
          case errors => Future.successful(FailureResult(errors.map(_.toMessageInfo(messages))))
        }
      case _ => block
    }
  }

  private def renderInvalidJsonMessages(invalid: Seq[(JsPath, Seq[ValidationError])])(implicit messages: Messages) = {
    Json.toJson(
      MessageResult(
        ok = false,
        messages = invalid flatMap { case (path, validationErrors) =>
          validationErrors map { validationError =>
            MessageInfo(
              key = Some(path.toJsonString),
              code = validationError.message,
              message = messages(validationError.message)
            )
          }
        }
      )
    )
  }

  private def processResult[A: ClassTag, C](outputFuture: Future[RestResult])(implicit writes: Writes[A]): Future[(Status, JsValue)] = {
    val clazz = implicitly[ClassTag[A]].runtimeClass
    outputFuture map {
      case OkResult(messageResult: MessageResult) => (Ok, Json.toJson(messageResult))
      case OkResult(data) if clazz.isInstance(data) => (Ok, okResult(data.asInstanceOf[A]))
      case OkResult(str: String) => (Ok, Json.obj("ok" -> s"$str"))
      case failureResult@FailureResult(Seq(MessageInfo(_, SystemMessages.AccessDenied, _))) => (Forbidden, Json.toJson(MessageResult.failure(failureResult)))
      case failureResult: FailureResult => (BadRequest, Json.toJson(MessageResult.failure(failureResult)))
    }
  }

  protected def okResult[A](data: A)(implicit writes: Writes[A]) = {
    Json.obj(
      "ok" -> true,
      "data" -> writes.writes(data)
    )
  }

}
