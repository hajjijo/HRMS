package employs.services

import javax.inject.Inject

import core.models.MessageKeyCode
import core.utils.SystemMessages
import employs.dao.EmployDao
import employs.model.{EmployEntity, EmployFullNameModel, EmployListModel, EmploysFullNameModel}
import play.api.libs.json.{Json, Writes}

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

  // implicit json formatters ...
  //EmployEntity JSON formatter
  implicit val employWrites = new Writes[EmployEntity] {
    def writes(employEntity: EmployEntity) = Json.obj(
      "name" -> employEntity.name,
      "family" -> employEntity.family,
      "nationalId" -> employEntity.nationalId,
      "zipCode" -> employEntity.zipCode,
      "phone" -> employEntity.phone,
      "address" -> employEntity.address,
      "employStatus" -> employEntity.employStatus,
      "salary" -> employEntity.salary,
      "id" -> employEntity.id
    )
  }

  //EmployListModel JSON formatter
  implicit val employsWrites = new Writes[EmployListModel] {
    def writes(employEntitys: EmployListModel) = Json.obj(
      "employs" -> employEntitys.employs
    )
  }

  def getById(employId: Long): Future[Option[EmployEntity]] = {
    employDao.findById(employId)
  }

  def listAll: Future[Seq[EmployEntity]] = {
    employDao.all
  }

  //EmployFullNameModel JSON formatter
  implicit val employFullNameModelWrites = new Writes[EmployFullNameModel] {
    def writes(employFullNameModel: EmployFullNameModel) = Json.obj(
      "id" -> employFullNameModel.id,
      "name" -> employFullNameModel.name,
      "family" -> employFullNameModel.family
    )
  }

  //EmploysFullNameModel JSON formatter
  implicit val employsFullNameModelWrites = new Writes[EmploysFullNameModel] {
    def writes(employsFullNameModel: EmploysFullNameModel) = Json.obj(
      "fullNames" -> employsFullNameModel.fullNames
    )
  }

  def listFullNames: Future[Seq[EmployFullNameModel]] = {
    employDao.fullNames
  }

}
