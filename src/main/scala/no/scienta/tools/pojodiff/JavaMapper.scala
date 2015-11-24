package no.scienta.tools.pojodiff

class JavaMapper(ref: AnyRef) {
  def toMap[V <: AnyRef]: java.util.Map[String, AnyRef] = Mapper(ref).toJava
}

