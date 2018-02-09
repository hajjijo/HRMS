package employs.dao

import javax.inject.{Inject, Singleton}

import employs.model.{EmployEntity, EmployFullNameModel, EmploysFullNameModel}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmployDao @Inject()(
                           protected val dbConfigProvider: DatabaseConfigProvider
                         )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val employTableQuery = TableQuery[EmployTable]

  def insert(employEntity: EmployEntity): Future[Long] = {
    db.run(employTableQuery returning employTableQuery.map(_.id) += employEntity)
  }

  def update(employEntity: EmployEntity): Future[Int] = {
    db.run(employTableQuery.filter(_.id === employEntity.id.get).update(employEntity))
  }

  def delete(id: Long): Future[Int] = {
    db.run(employTableQuery.filter(_.id === id).delete)
  }

  def findById(id: Long): Future[Option[EmployEntity]] = {
    db.run(employTableQuery.filter(_.id === id).result.headOption)
  }

  def all: Future[Seq[EmployEntity]] = {
    db.run(employTableQuery.result)
  }

  def fullNames: Future[Seq[EmployFullNameModel]] = {
    val queryResult = db.run(employTableQuery.map(employ => (employ.id, employ.name, employ.family)).result)
    queryResult map { basicEmploys =>
      basicEmploys map { basicEmploy =>
          EmployFullNameModel(basicEmploy._1, basicEmploy._2, basicEmploy._3)
      }
    }
  }

  @Singleton
  final class EmployTable(tag: Tag) extends Table[EmployEntity](tag, "employs") {
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
    ).shaped <> ((EmployEntity.apply _).tupled, EmployEntity.unapply)
  }

}
