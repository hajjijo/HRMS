package daos

import javax.inject.{Inject, Singleton}

import models.EmployEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmployDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val employTableQuery = TableQuery[UserTable]

  def insert(employEntity: EmployEntity): Future[Long] = {
    db.run(employTableQuery returning employTableQuery.map(_.id) += employEntity)
  }


  @Singleton
  final class UserTable(tag: Tag) extends Table[EmployEntity](tag, "employs") {
    def name = column[String]("name")
    def family = column[String]("family")
    def nationalId = column[String]("nationalId")
    def zipCode = column[String]("zipCode")
    def phone = column[String]("phone")
    def address = column[String]("address")
    def employStatus = column[String]("employStatus")
    def salary = column[Long]("salary")
    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def * = (
      name,
      family,
      nationalId,
      zipCode,
      phone,
      address,
      employStatus,
      salary,
      id.?
    ).shaped <> (EmployEntity.tupled, EmployEntity.unapply)
  }
}
