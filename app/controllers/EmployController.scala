package controllers

import javax.inject.Inject
import models.EmployEntity
import play.api.mvc.{Action, Controller}
import services.EmployService
import scala.concurrent.{ExecutionContext, Future}

class EmployController @Inject()(employService: EmployService)(implicit exec: ExecutionContext) extends Controller {

  // http://localhost:9000/api/hrms/v1/employ/add
  //POST Json=> {"name":"nameTest","family":"familyTest","nationalId":"001945356","zipCode":"1025456","phone":"0912123567","address":"add-res-test","employStatus":"programmer","salary":10000}
  def addEmploy = Action.async(parse.json) { request => {
    for {
      name <- (request.body \ "name").asOpt[String]
      family <- (request.body \ "family").asOpt[String]
      nationalId <- (request.body \ "nationalId").asOpt[String]
      zipCode <- (request.body \ "zipCode").asOpt[String]
      phone <- (request.body \ "phone").asOpt[String]
      address <- (request.body \ "address").asOpt[String]
      employStatus <- (request.body \ "employStatus").asOpt[String]
      salary <- (request.body \ "salary").asOpt[Long]
    } yield {

      val employ = EmployEntity(
        name = name,
        family = family,
        nationalId = nationalId,
        zipCode = zipCode,
        phone = phone,
        address = address,
        employStatus = employStatus,
        salary = salary
      )
      employService.addEmploy(employ).map(Ok(_))
    }
  }.getOrElse(Future {
    BadRequest(s"""{"ok":"false","message":"Wrong json format"}""")
  })
  }

  // http://localhost:9000/api/hrms/v1/employs/:id<for example (2)>/edit
  //PUT Json=> {"name":"nameTest2","family":"familyTest2","nationalId":"00194000","zipCode":"1025000","phone":"09120000","address":"add-res-test-2","employStatus":"CTO","salary":1000000}
  def editEmploy(employId: Long) = Action.async(parse.json) { request => {
    for {
      name <- (request.body \ "name").asOpt[String]
      family <- (request.body \ "family").asOpt[String]
      nationalId <- (request.body \ "nationalId").asOpt[String]
      zipCode <- (request.body \ "zipCode").asOpt[String]
      phone <- (request.body \ "phone").asOpt[String]
      address <- (request.body \ "address").asOpt[String]
      employStatus <- (request.body \ "employStatus").asOpt[String]
      salary <- (request.body \ "salary").asOpt[Long]
    } yield {

      val employ = EmployEntity(
        id = Some(employId),
        name = name,
        family = family,
        nationalId = nationalId,
        zipCode = zipCode,
        phone = phone,
        address = address,
        employStatus = employStatus,
        salary = salary
      )
      employService.editEmploy(employ).map(Ok(_))
    }
  }.getOrElse(Future {
    BadRequest(s"""{"ok":"false","message":"Wrong json format"}""")
  })
  }

  // http://localhost:9000/api/hrms/v1/employs/:id<for example (2)>/delete
  //DELETE
  def deleteEmploy(employId: Long) = Action.async {
    employService.deleteEmploy(employId).map(Ok(_))
  }

  // http://localhost:9000/api/hrms/v1/employs/:id<for example (2)>/get
  //GET
  def getEmployById(employId: Long) = Action.async {
    employService.getById(employId).map(Ok(_))
  }

  // http://localhost:9000/api/hrms/v1/employs/get
  //GET
  def getEmploys = Action.async {
    employService.listAll.map(Ok(_))
  }

  // http://localhost:9000/api/hrms/v1/employs/full-names
  //GET
  def fullNames = Action.async {
    employService.listFullNames.map(Ok(_))
  }

}
