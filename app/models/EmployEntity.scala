package models

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
