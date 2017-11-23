package services

import javax.inject.Inject
import daos.RollCallDao
import models.RollCallEntity
import org.joda.time.DateTime
import scala.concurrent.{ExecutionContext, Future}

class RollCallService @Inject()(rollCallDao: RollCallDao)(implicit val ec: ExecutionContext) {

  def present(employ_id: Long): Future[String] = {
    val rollCallModel = RollCallEntity(employ_id, DateTime.now)

    rollCallDao.insert(rollCallModel) flatMap {
      case 0 => Future.successful("""{"ok":"false","message":"operation failed !!!"}""")
      case _ => Future.successful("""{"ok":"true","message":"operation successful ..."}""")
    }
  }

  def exit(employ_id: Long): Future[String] = {
    rollCallDao.findLastPresentByEmployId(employ_id) flatMap {
      case None => Future.successful("""{"ok":"false","message":"message":"not found !!!"}""")
      case Some(rollCall) =>
        val rollCallEditModel = rollCall.copy(out_date = Some(DateTime.now))
        rollCallDao.setExitTime(rollCallEditModel) flatMap {
          case 0 => Future.successful("""{"ok":"false","message":"operation failed !!!"}""")
          case _ => Future.successful("""{"ok":"true","message":"operation successful ..."}""")
        }

    }
  }

}
