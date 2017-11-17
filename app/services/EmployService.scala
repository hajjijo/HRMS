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
          case false => employDao.insert(employ).map(newId => s"""{"ok":"true","id":"$newId"}""")
        }
      }
    }

  }

}
