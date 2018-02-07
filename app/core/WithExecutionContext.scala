package core
import scala.concurrent.ExecutionContext

trait WithExecutionContext {
  implicit def ec: ExecutionContext
}
