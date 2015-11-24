package no.scienta.tools.pojodiff

import java.lang.reflect.Field

import scala.collection.immutable.TreeMap

case class Mapper(refMap: RefMap)  {

  def subTree(fieldType: FieldType): RefMap = refMap filterKeys (_ is fieldType)

  import scala.collection.JavaConversions._

  def toJava: java.util.Map[String, AnyRef] = Map[String, AnyRef](refMap.toList map {
    case (RefKey(field, fieldType), value) => (field.getName, fieldType javaValue value)
  }: _*)
}

object Mapper {

  def apply(values: Iterable[RefValue]): Mapper = Mapper(TreeMap[RefKey, AnyRef](values.map({
      case RefValue(f, ft, ref) => (RefKey(f, ft), ref)
    }).toList: _*))

  def apply(ref: AnyRef): Mapper = {
    val values: List[(RefKey, AnyRef)] =
      superclassPath(ref) flatMap fieldList map toAccessible map withValue(ref) filter hasValue map nestify(ref)
    Mapper(TreeMap(values: _*))
  }

  private def hasValue(fieldValue: (Field, AnyRef)) = Option(fieldValue._2).isDefined

  private def superclassPath(ref: AnyRef): List[Class[_]] = {
    def typeHierarchy(t: Class[_]): List[Class[_]] =
      if (t == classOf[java.lang.Object]) Nil else t :: typeHierarchy(t.getSuperclass)
    typeHierarchy(ref.getClass)
  }

  private def fieldList(t: Class[_]) = t.getDeclaredFields.toList filterNot isInternal

  private def isInternal(field: Field) = field.getName contains "$"

  private def toAccessible(field: Field): Field = {
    field setAccessible true
    field
  }

  private def withValue(ref: AnyRef)(field: Field): (Field, AnyRef) = (field, field get ref)

  private def nestify(ref: AnyRef)(fieldValue: (Field, AnyRef)): (RefKey, AnyRef) = fieldValue match {
    case (f, value) if isMapLike(value) => (RefKey(f, MapField), valueAsMap(value))
    case (f, value) if isSequenceLike(value) => (RefKey(f, ListField), valueAsList(value))
    case (f, value) if isLeaf(value) => (RefKey(f, LeafField), value)
    case (f, value) => (RefKey(f, NodeField), Mapper(value))
  }
}
