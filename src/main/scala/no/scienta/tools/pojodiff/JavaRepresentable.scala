package no.scienta.tools.pojodiff

trait JavaRepresentable[T] {
  def toJava: T
}
