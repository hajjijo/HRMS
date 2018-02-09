package report.services

import javax.inject.Inject

import play.api.libs.json.{Json, Writes}
import report.dao.ReportDao
import report.model.entity.SumSalaryReportModel
import rollcall.model.RollCallEntity

import scala.concurrent.{ExecutionContext, Future}

class ReportService @Inject()(reportDao: ReportDao)(implicit val ec: ExecutionContext) {

  //SumSalaryModel JSON formatter
  implicit val sumSalaryModelWrites = new Writes[SumSalaryReportModel] {
    def writes(sumSalaryModel: SumSalaryReportModel) = Json.obj(
      "employ_id" -> sumSalaryModel.employ_id,
      "name" -> sumSalaryModel.name,
      "family" -> sumSalaryModel.family,
      "sum_time" -> sumSalaryModel.in_time,
      "sum_salary" -> sumSalaryModel.sumSalary
    )
  }

  //TODO Kian: Please fix this block of code ...
  def getSumSalary: Future[String] = {
    reportDao.getRollCalls flatMap {
      case Nil =>
        Future.successful("""{"ok":"false","message":"not found!"}""")
      case rollCalls =>
        val employId_present_time = calculateEmploysPresentTime(rollCalls)

        reportDao.getEmployFullName flatMap {
          case Nil =>
            Future.successful("""{"ok":"false","message":"not found!"}""")
          case employs =>
            val sumSalaries = employs map { employ =>
              val employSumPresentTime = employId_present_time.get(employ._1)
              SumSalaryReportModel(employ._1, employ._2, employ._3, employSumPresentTime.getOrElse(0), (employ._4 * employSumPresentTime.getOrElse(0.0)))
            }
            val sumSalariesJson = Json.toJson(sumSalaries)
            Future.successful(s"""{"ok":"true","result":$sumSalariesJson}""")
        }
    }
  }

  private def calculateEmploysPresentTime(rollCalls: Seq[RollCallEntity]): Map[Long, Double] = {
    val employsPresentTimeTuple = rollCalls map { rollCall =>
      val calculatePresentsTime: Double = (rollCall.out_date.get.getMillis - rollCall.in_date.getMillis) / 1000.0 / 60.0 / 60.0
      val calculatePresentsTimePrecision = "%.1f".format(calculatePresentsTime).toDouble
      (rollCall.employ_id, calculatePresentsTimePrecision)
    }
    employsPresentTimeTuple.groupBy(_._1) mapValues (_ map (_._2)) map { case (employ_id, presentTimes) =>
      (employ_id, presentTimes.reduce(_ + _))
    }
  }

}
