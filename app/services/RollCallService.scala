package services

import javax.inject.Inject

import daos.RollCallDao
import models.RollCallEntity
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

class RollCallService @Inject()(dateTestDao: RollCallDao)(implicit val ec: ExecutionContext) {

  def present(employ_id: Long): Future[String] = {
    val rollCallModel = RollCallEntity(employ_id, DateTime.now)

    dateTestDao.insert(rollCallModel) flatMap {
      case 0 => Future.successful("""{"ok":"false","message":"operation failed !!!"}""")
      case _ => Future.successful("""{"ok":"true","operation successful ...}""")
    }
  }

//  def get(id: Long): Future[String] = {
//    dateTestDao.get(id) flatMap {
//      case None =>
//        Future.successful("""false""")
//      case Some(dateTest) =>
//        val a = dateTest.date.hourOfDay.get
//        Future.successful(s"""this is your result : ${a}""")
//    }
//  }

}
