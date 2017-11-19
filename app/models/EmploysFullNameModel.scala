package models

case class EmploysFullNameModel(
                                 fullNames: Seq[EmployFullNameModel]
                               )

case class EmployFullNameModel(
                                id: Long,
                                name: String,
                                family: String
                              )