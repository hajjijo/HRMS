package core.models

import play.api.i18n.Messages

trait RestResult

case class OkResult[A](result: A) extends RestResult

case class FailureResult(messages: Seq[MessageInfo]) extends RestResult

object FailureResult {
  def apply(code: String)(implicit messages: Messages): FailureResult = {
    FailureResult(Seq(MessageInfo.from(code)))
  }

  def apply(codes: Seq[String])(implicit messages: Messages): FailureResult = {
    FailureResult(codes.map(MessageInfo.from(_)))
  }
}
