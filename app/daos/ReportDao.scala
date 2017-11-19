package daos

import javax.inject.Inject
import models.RollCallEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

class ReportDao @Inject()(
                           val employDao: EmployDao,
                           val rollCallDao: RollCallDao,
                           protected val dbConfigProvider: DatabaseConfigProvider
                         )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def getRollCalls: Future[Seq[RollCallEntity]] = {
    db.run(rollCallDao.rollCallTableQuery.filter(_.out_date.isDefined).result)
  }

  def getEmployFullName: Future[Seq[(Long, String, String, Long)]] = {
    db.run(employDao.employTableQuery.map(employs => (employs.id, employs.name, employs.family, employs.salary)).result)
  }

}
