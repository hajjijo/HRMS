package core

import play.api.i18n.Messages
import play.api.libs.json.Json

case class MessageResult(ok: Boolean, messages: Seq[MessageInfo])

case class MessageInfo(
                        key: Option[String],
                        code: String,
                        message: String
                      )

object MessageInfo {
  implicit val formatter = Json.format[MessageInfo]

  def from(key: String, code: String)(implicit messages: Messages): MessageInfo = {
    apply(Some(key), code, messages(code))
  }

  def from(code: String)(implicit messages: Messages): MessageInfo = {
    apply(None, code, messages(code))
  }
}

object MessageResult extends JsonFormatter[MessageResult] {
  implicit val formatter = Json.format[MessageResult]
  def success(code: String)(implicit messages: Messages) = MessageResult(true, Seq(MessageInfo.from(code)))
  def failure(failureResult: FailureResult) = MessageResult(false, failureResult.messages)
}

case class MessageKeyCode(
                           key: Option[String],
                           code: String
                         ) {
  def toMessageInfo(implicit messages: Messages) = {
    MessageInfo(key, code, messages(code))
  }
}

object MessageKeyCode extends JsonFormatter[MessageKeyCode] {
  implicit val formatter = Json.format[MessageKeyCode]
}