package no.scienta.tools.pojodiff.utils


import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.reflect._

class Cascading[T](t: T) {

  /**
   * Any operation that takes a single t.
   */
  type SideEffect = T => Any

  /**
   * Perform the side effect on t and return t.
   *
   * @param sideEffect Side effect to be performed
   * @return t
   */
  def withSideEffect(sideEffect: SideEffect): T = withSideEffects(sideEffect)

  /**
   * Perform the side effects on t and return t.
   *
   * @param sideEffects Side effects to be performed
   * @return t
   */
  def withSideEffects(sideEffects: SideEffect*): T = {
    sideEffects foreach (_ apply t)
    t
  }

  def logger[C: ClassTag]: Logger = logger(classTag[C].runtimeClass.asInstanceOf[Class[C]])

  def logger(clazz: Class[_]): Logger = Logger(LoggerFactory getLogger clazz)
}
