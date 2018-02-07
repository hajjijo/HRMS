package core

import play.api.libs.json.Format

trait JsonFormatter[T] {
  def formatter: Format[T]
}
