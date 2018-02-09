package core.utils

import play.api.libs.json.Format

trait JsonFormatter[T] {
  def formatter: Format[T]
}
