package no.scienta.tools.pojodiff

object Nested {

  def apply(value: AnyRef): Nested = value match {
    case v if isMapLike(v) => Nested(MapField, valueAsMap(value))
    case v if isSequenceLike(v) => Nested(ListField, valueAsList(value) map Nested.apply)
    case v if isLeaf(v) => Nested(LeafField, v)
    case v => Nested(NodeField, Mapper(value));
  }
}

case class Nested(fieldType: FieldType, value: AnyRef) {

  def toJava = fieldType javaValue value
}
