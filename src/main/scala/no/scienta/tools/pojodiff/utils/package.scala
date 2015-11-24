package no.scienta.tools.pojodiff

import java.util.UUID

import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.language.implicitConversions
import scala.reflect._

package object utils {

  implicit def t2CascadingT[T](t: T): Cascading[T] = new Cascading(t)

  def loggerFor[C: ClassTag]: Logger = loggerFor(classTag[C].runtimeClass.asInstanceOf[Class[C]])

  def loggerFor(any: AnyRef): Logger = loggerFor(any.getClass)

  def loggerFor(clazz: Class[_]): Logger = Logger(LoggerFactory getLogger clazz)

  def loggerFor(name: String): Logger = Logger(LoggerFactory getLogger name)

  implicit def marginalizedString(margin: Margin): String = margin.str.stripMargin

  implicit def stringToUUID(str: String): UUID = UUID fromString str

  implicit def stringToUUID(str: Option[String]): Option[UUID] = str map UUID.fromString
}
