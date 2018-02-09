package core.models

trait Validatable {
  def getValidationErrors(): Seq[MessageKeyCode]
}
