package core

trait Validatable {
  def getValidationErrors(): Seq[MessageKeyCode]
}
