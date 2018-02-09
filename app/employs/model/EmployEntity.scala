package employs.model

import core.models.{MessageKeyCode, Validatable}
import core.utils.{JsonFormatter, SystemMessages}
import play.api.libs.json.Json

case class EmployEntity(
                         name: String,
                         family: String,
                         nationalId: String,
                         zipCode: String,
                         phone: String,
                         address: String,
                         employStatus: String,
                         salary: Long,
                         id: Option[Long] = None
                       ) extends Validatable {

  override def getValidationErrors(): Seq[MessageKeyCode] = {

    val nameCheck = if (name.isEmpty) {
      Seq(MessageKeyCode(Some("name"), SystemMessages.NameCannotBeEmpty))
    } else {
      Nil
    }

    val familyCheck = if (family.isEmpty) {
      Seq(MessageKeyCode(Some("family"), SystemMessages.FamilyCannotBeEmpty))
    } else {
      Nil
    }

    val nationalIdCheckEmpty = if (nationalId.isEmpty) {
      Seq(MessageKeyCode(Some("nationalId"), SystemMessages.NationalIdCannotBeEmpty))
    } else {
      Nil
    }

    val nationalIdCheckValidate = if (isValidIranianNationalId(nationalId)) {
      Seq(MessageKeyCode(Some("nationalId"), SystemMessages.NationalIdUnsupportedFormat))
    } else {
      Nil
    }

    val zipCodeCheckEmpty = if (zipCode.isEmpty) {
      Seq(MessageKeyCode(Some("zipCode"), SystemMessages.zipCodeCannotBeEmpty))
    } else {
      Nil
    }

    val zipCodeCheckValidate = if(!isValidIranianZipCode(zipCode)) {
      Seq(MessageKeyCode(Some("zipCode"), SystemMessages.zipCodeCannotBeEmpty))
    } else {
      Nil
    }

    val addressCheck = if (address.isEmpty) {
      Seq(MessageKeyCode(Some("address"), SystemMessages.addressCannotBeEmpty))
    } else {
      Nil
    }

    val employStatusCheck = if (employStatus.isEmpty) {
      Seq(MessageKeyCode(Some("employStatus"), SystemMessages.employStatusCannotBeEmpty))
    } else {
      Nil
    }

    val salaryCheckValidate = if (salary < 7000) {
      Seq(MessageKeyCode(Some("salary"), SystemMessages.salaryMustBiggerThan7000RI))
    } else {
      Nil
    }

    val phoneCheck = if (phone.isEmpty) {
      Seq(MessageKeyCode(Some("phone"), SystemMessages.phoneCannotBeEmpty))
    } else {
      Nil
    }

    nameCheck ++
      familyCheck ++
      nationalIdCheckEmpty ++
      nationalIdCheckValidate ++
      zipCodeCheckEmpty ++
      zipCodeCheckValidate ++
      addressCheck ++
      employStatusCheck ++
      salaryCheckValidate ++
      phoneCheck

  }

  // copy from : https://gist.github.com/ebraminio/5292017
  def isValidIranianNationalId(input: String): Boolean = {
    val pattern = """^(\d{10})$""".r
    input match {
      case pattern(_) =>
        val check = input.substring(9, 10).toInt
        val sum = (0 to 8)
          .map(x => input.substring(x, x + 1).toInt * (10 - x))
          .sum % 11

        (sum < 2 && check == sum) || (sum >= 2 && check + sum == 11)
      case _ => false
    }
  }

  def isValidIranianZipCode(input: String): Boolean = {
    val pattern = """/^[0-9]*$/""".r
    input match {
      case pattern => input.length == 10
      case _ => false
    }
  }

}

object EmployEntity extends JsonFormatter[EmployEntity] {
  implicit val formatter = Json.format[EmployEntity]
}
