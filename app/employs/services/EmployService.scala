package employs.services

import javax.inject.Inject

import core.models.MessageKeyCode
import employs.dao.EmployDao
import employs.model.{EmployEntity, EmployFullNameModel}

import scala.concurrent.{ExecutionContext, Future}

class EmployService @Inject()(employDao: EmployDao)(implicit val ec: ExecutionContext) {

  def addEmploy(employ: EmployEntity): Future[Either[Seq[MessageKeyCode], Long]] = {
    employ.getValidationErrors() match {
      case Nil => employDao.insert(employ).map(Right(_))
      case messages => Future.successful(Left(messages))
    }
  }

  def editEmploy(employId: Long, employ: EmployEntity): Future[Either[Seq[MessageKeyCode], Int]] = {
    employ.getValidationErrors() match {
      case Nil => employDao.update(employ.copy(id = Some(employId))).map(Right(_))
      case messages => Future.successful(Left(messages))
    }
  }

  def deleteEmploy(employId: Long): Future[Int] = {
    employDao.delete(employId)
  }

  def getById(employId: Long): Future[Option[EmployEntity]] = {
    employDao.findById(employId)
  }

  def listAll: Future[Seq[EmployEntity]] = {
    employDao.all
  }

  def listFullNames: Future[Seq[EmployFullNameModel]] = {
    employDao.fullNames
  }

}
