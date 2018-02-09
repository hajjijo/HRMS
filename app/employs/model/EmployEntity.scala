package employs.model

import core.utils.JsonFormatter
import play.api.libs.json.Json

case class EmployEntity(
                         name: String,
                         family: String,
                         nationalId: String,
                         zipCode: String,
                         phone: String,
                         address: String,
                         employStatus: String,
                         salary: Long,
                         id: Option[Long] = None
                       )

object EmployEntity extends JsonFormatter[EmployEntity] {
  override def formatter = Json.format[EmployEntity]
}
