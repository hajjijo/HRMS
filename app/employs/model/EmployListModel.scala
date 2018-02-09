package employs.model

import core.utils.JsonFormatter
import play.api.libs.json.{Format, Json}

case class EmployListModel(
                            employs: Seq[EmployEntity]
                          )
object EmployListModel extends JsonFormatter[EmployListModel]{
  implicit val EmployEntityFormatter: Format[EmployEntity] = Json.format[EmployEntity]
  implicit val formatter: Format[EmployListModel] = Json.format[EmployListModel]
}
