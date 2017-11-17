package services

import javax.inject.Inject
import daos.EmployDao
import models.EmployEntity
import scala.concurrent.{ExecutionContext, Future}

class EmployService @Inject()(employDao: EmployDao)(implicit val ec: ExecutionContext) {

  def addEmploy(employ: EmployEntity): Future[String] = {
    (employ.name.isEmpty || employ.family.isEmpty) match {
      case true => Future.successful("""{"ok":"false","message":"name or family is empty. please fill it"}""")
      case false => (employ.nationalId.isEmpty || employ.zipCode.isEmpty || employ.address.isEmpty || employ.employStatus.isEmpty || employ.phone.isEmpty) match {
        case true => Future.successful("""{"ok":"false","message":"please fill the empty fields"}""")
        case false => (employ.salary < 7000) match {
          case true => Future.successful("""{"ok":"false","message":"the employ salary must bigger than employs low"}""")
          case false => employDao.insert(employ).map(newId => s"""{"ok":"true","message":"operation success full","id":"$newId"}""")
        }
      }
    }
  }

  def editEmploy(employ: EmployEntity): Future[String] = {
    (employ.id.isEmpty) match {
      case true => Future.successful("""{"ok":"false","message","id field is empty"}""")
      case false => (employ.name.isEmpty || employ.family.isEmpty) match {
        case true => Future.successful("""{"ok":"false","message":"name or family is empty. please fill it"}""")
        case false => (employ.nationalId.isEmpty || employ.zipCode.isEmpty || employ.address.isEmpty || employ.employStatus.isEmpty || employ.phone.isEmpty) match {
          case true => Future.successful("""{"ok":"false","message":"please fill the empty fields"}""")
          case false => (employ.salary < 7000) match {
            case true => Future.successful("""{"ok":"false","message":"the employ salary must bigger than employs low"}""")
            case false => employDao.update(employ).map(many => s"""{"ok":"true","message":"${many} row is edited"}""")
          }
        }
      }
    }
  }

  def deleteEmploy(employId: Long): Future[String] = {
    employDao.delete(employId) flatMap {
      case 0 => Future.successful("""{"ok":"false","message":"operation failed !!!"}""")
      case _ => Future.successful("""{"ok":"false","message":"operation successful"}""")
    }
  }

}
