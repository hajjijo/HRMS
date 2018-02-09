package rollcall.dao

import javax.inject.{Inject, Singleton}

import com.github.tototoshi.slick.PostgresJodaSupport._
import employs.dao.EmployDao
import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import rollcall.model.RollCallEntity
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RollCallDao @Inject()(
                             val employDao: EmployDao,
                             protected val dbConfigProvider: DatabaseConfigProvider
                           )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  val rollCallTableQuery = TableQuery[RollCallTable]

  def insert(dateTest: RollCallEntity): Future[Long] = {
    db.run(rollCallTableQuery returning rollCallTableQuery.map(_.id) += dateTest)
  }

  //TODO Kian: please fix this method ...
  def findLastPresentByEmployId(employ_id: Long): Future[Option[RollCallEntity]] = {
    db.run(rollCallTableQuery.filter(_.employ_id === employ_id).size.result) flatMap { size =>
      db.run(rollCallTableQuery.filter(_.employ_id === employ_id).drop(size - 1).result.headOption)
    }
  }

  def setExitTime(rollCallEntity: RollCallEntity): Future[Int] = {
    db.run(rollCallTableQuery.filter(_.id === rollCallEntity.id).update(rollCallEntity))
  }

  @Singleton
  final class RollCallTable(tag: Tag) extends Table[RollCallEntity](tag, "roll_call") {
    def employ_id = column[Long]("employ_id")
    def in_date = column[DateTime]("in_date")
    def out_date = column[Option[DateTime]]("out_date")
    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def * = (
      employ_id,
      in_date,
      out_date,
      id.?
    ).shaped <> (RollCallEntity.tupled, RollCallEntity.unapply)

    def employ = foreignKey("fk_RollCall_EmployId", employ_id, employDao.employTableQuery)(_.id, onDelete = ForeignKeyAction.Cascade)
  }

}
