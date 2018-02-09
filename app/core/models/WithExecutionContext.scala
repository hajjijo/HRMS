package core.models

import scala.concurrent.ExecutionContext

trait WithExecutionContext {
  implicit def ec: ExecutionContext
}
