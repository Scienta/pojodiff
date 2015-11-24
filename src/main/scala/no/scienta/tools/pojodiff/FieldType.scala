package no.scienta.tools.pojodiff

import scala.collection.immutable.SortedMap
import scala.collection.JavaConversions._

sealed trait FieldType extends JavaConverter

case object LeafField extends FieldType {
  override def javaValue(v: AnyRef): AnyRef = v
}

case object MapField extends FieldType {
  override def javaValue(v: AnyRef): java.util.Map[String, AnyRef] =
    v.asInstanceOf[SortedMap[String, Nested]] mapValues (_.toJava)
}

case object ListField extends FieldType {
  override def javaValue(v: AnyRef): java.util.List[AnyRef] =
    v.asInstanceOf[Iterable[Nested]].map(_.toJava).toList
}

case object NodeField extends FieldType {
  override def javaValue(v: AnyRef): java.util.Map[String, AnyRef] =
    v.asInstanceOf[Mapper].toJava
}
