package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import services.ReportService

import scala.concurrent.ExecutionContext

class ReportController @Inject()(reportService: ReportService)(implicit exec: ExecutionContext) extends Controller {

  // HTTP Method : GET , URL : http://localhost:9000/api/hrms/v1/reports/sum-salary
  def sumSalary = Action.async {
    reportService.getSumSalary.map(Ok(_))
  }

}
