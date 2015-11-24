package no.scienta.tools

import java.lang.reflect.Field
import java.util

import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.collection.SortedMap
import scala.collection.immutable.TreeMap
import scala.language.implicitConversions
import scala.reflect._

package object pojodiff {

  case class RefValue(field: Field, fieldType: FieldType, ref: AnyRef) {

    def key = RefKey(field, fieldType)
  }

  case class RefKey(field: Field, fieldType: FieldType) extends ((RefMap) => RefValue) {

    def apply(map: RefMap) = RefValue(field, fieldType, map(this))

    def is(ft: FieldType) = fieldType == ft
  }

  type RefMap = SortedMap[RefKey, AnyRef]

  implicit val nameOrdering: Ordering[String] = Ordering.String

  implicit val keyOrdering: Ordering[RefKey] = Ordering by ((refKey: RefKey) => refKey.field.getName)

  implicit val valueOrdering: Ordering[RefValue] = Ordering by ((refKey: RefValue) => refKey.field.getName)

  def refKey(refValue: RefValue): RefKey = refValue.key

  def isSequenceLike(value: AnyRef) = value.isInstanceOf[java.lang.Iterable[_]] || value.getClass.isArray

  def isMapLike(value: AnyRef) = value.isInstanceOf[java.util.Map[_, _]]

  def valueAsMap(value: AnyRef): SortedMap[String, Nested] =
    TreeMap(value.asInstanceOf[util.Map[String, AnyRef]].toMap.toList map {
      case (k, v) => (k, Nested(v))
    }: _*)

  def valueAsList(value: AnyRef): List[Nested] =
    (if (value.getClass.isArray) value.asInstanceOf[Array[AnyRef]].toList
    else if (value.isInstanceOf[java.lang.Iterable[_]]) value.asInstanceOf[java.lang.Iterable[AnyRef]].toList
    else sys error s"Expected list-like value, got $value of ${value.getClass}") map Nested.apply

  def isLeaf(value: AnyRef) = Option(value) map (_.getClass) exists isLeafType

  def isLeafType(t: Class[_]) = (wrapperTypes contains t) || (t.getPackage.getName startsWith "java.")

  private val wrapperTypes = List(
    classOf[Boolean], classOf[Integer], classOf[Long], classOf[Double], classOf[Float], classOf[Byte], classOf[Short])

  def loggerFor[C: ClassTag]: Logger = loggerFor(classTag[C].runtimeClass.asInstanceOf[Class[C]])

  def loggerFor(clazz: Class[_]): Logger = Logger(LoggerFactory getLogger clazz)
}
