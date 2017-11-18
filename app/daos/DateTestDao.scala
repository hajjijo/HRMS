package daos

import javax.inject.{Inject, Singleton}
import com.github.tototoshi.slick.PostgresJodaSupport._
import models.DateTest
import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DateTestDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  val dateTableQuery = TableQuery[DateTable]

  def insert(dateTest: DateTest): Future[Long] = {
    db.run(dateTableQuery returning dateTableQuery.map(_.id) += dateTest)
  }

  def get(id: Long): Future[Option[DateTest]] = {
    db.run(dateTableQuery.filter(_.id === id).result.headOption)
  }

  @Singleton
  final class DateTable(tag: Tag) extends Table[DateTest](tag, "dateTable") {

    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def date = column[DateTime]("date")

    def * = (
      date,
      id.?
      ).shaped <> (DateTest.tupled, DateTest.unapply)
  }

}
