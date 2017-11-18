package services

import javax.inject.Inject

import daos.DateTestDao
import models.DateTest

import scala.concurrent.{ExecutionContext, Future}

class DateTestService @Inject()(dateTestDao: DateTestDao)(implicit val ec: ExecutionContext) {

  def insert (entity: DateTest): Future[String] = {
    dateTestDao.insert(entity) flatMap {
      case 0l => Future.successful("""false""")
      case _ => Future.successful("""true""")
    }
  }

  def get(id: Long): Future[String] = {
    dateTestDao.get(id) flatMap {
      case None =>
        Future.successful("""false""")
      case Some(dateTest) =>
        val a = dateTest.date.hourOfDay.get
        Future.successful(s"""this is your result : ${a}""")
    }
  }

}
