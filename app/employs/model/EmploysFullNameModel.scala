package employs.model

import core.utils.JsonFormatter
import play.api.libs.json.Json

case class EmploysFullNameModel(
                                 fullNames: Seq[EmployFullNameModel]
                               )

case class EmployFullNameModel(
                                id: Long,
                                name: String,
                                family: String
                              )

object EmployFullNameModel extends JsonFormatter[EmployFullNameModel] {
  implicit val formatter = Json.format[EmployFullNameModel]
}

object EmploysFullNameModel extends JsonFormatter[EmploysFullNameModel] {
  implicit val employFullNameModelFormatter = Json.format[EmployFullNameModel]
  implicit val formatter = Json.format[EmploysFullNameModel]
}
