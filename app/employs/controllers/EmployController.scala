package employs.controllers

import javax.inject.Inject

import core.controllers.ApiController
import core.models.{FailureResult, MessageResult, OkResult}
import core.utils.SystemMessages
import employs.model.{EmployEntity, EmployListModel, EmploysFullNameModel}
import employs.services.EmployService
import play.api.i18n.MessagesApi

import scala.concurrent.ExecutionContext

class EmployController @Inject()(
                                  employService: EmployService,
                                  val messagesApi: MessagesApi,
                                  implicit val ec: ExecutionContext
                                ) extends ApiController {

  //POST => http://localhost:9000/api/hrms/v1/employ/add
  /*
  {
  "name": "nameTest",
  "family": "familyTest",
  "nationalId": "001945356",
  "zipCode": "1025456",
  "phone": "0912123567",
  "address": "add-res-test",
  "employStatus": "programmer",
  "salary": 10000
  }
  */
  def addEmploy = restAction(EmployEntity, MessageResult) { implicit request =>
    employService.addEmploy(request.model) map {
      case Left(errors) => FailureResult(errors.map(_.toMessageInfo))
      case Right(_) => OkResult(MessageResult.successful)
    } recover {
      case _: Throwable => FailureResult(SystemMessages.OperationFailed)
    }
  }

  //PUT => http://localhost:9000/api/hrms/v1/employs/1/edit
  /*
  {
  "name": "nameTest2",
  "family": "familyTest2",
  "nationalId": "00194000",
  "zipCode": "1025000",
  "phone": "09120000",
  "address": "add-res-test-2",
  "employStatus": "CTO",
  "salary": 1000000
  }
  */
  def editEmploy(employId: Long) = restAction(EmployEntity, MessageResult) { implicit request =>
    employService.editEmploy(employId, request.model) map {
      case Right(updateCount) => updateCount match {
        case 0 => FailureResult(SystemMessages.OperationFailed)
        case _ => OkResult(MessageResult.successful)
      }
      case Left(errors) => FailureResult(errors.map(_.toMessageInfo))
    } recover {
      case _: Throwable => FailureResult(SystemMessages.OperationFailed)
    }
  }

  //DELETE => http://localhost:9000/api/hrms/v1/employs/1/delete
  def deleteEmploy(employId: Long) = restAction(MessageResult) { implicit request =>
    employService.deleteEmploy(employId) map {
      case 1 => OkResult(MessageResult.successful)
      case _ => FailureResult(SystemMessages.OperationFailed)
    } recover {
      case _: Throwable => FailureResult(SystemMessages.OperationFailed)
    }
  }

  //GET => http://localhost:9000/api/hrms/v1/employs/1/get
  def getEmployById(employId: Long) = restAction(EmployEntity) { implicit request =>
    employService.getById(employId) map {
      case None => FailureResult(SystemMessages.NotFound)
      case Some(employ) => OkResult(employ)
    } recover {
      case _: Throwable => FailureResult(SystemMessages.OperationFailed)
    }
  }

  //GET => http://localhost:9000/api/hrms/v1/employs/get
  //TODO => براش برو یه نگاهی به کد های بخش این بندازPaging __$__ FoodAdminController
  def getEmploys = restAction(EmployListModel) { implicit request =>
    employService.listAll map {
      case Nil => FailureResult(SystemMessages.NotFound)
      case employs => OkResult(EmployListModel(employs))
    } recover {
      case _: Throwable => FailureResult(SystemMessages.OperationFailed)
    }
  }

  //GET => http://localhost:9000/api/hrms/v1/employs/full-names
  def fullNames = restAction(EmploysFullNameModel) { implicit request =>
    employService.listFullNames map {
      case Nil => FailureResult(SystemMessages.NotFound)
      case fullNames => OkResult(fullNames)
    } recover {
      case _: Throwable => FailureResult(SystemMessages.OperationFailed)
    }
  }

}
