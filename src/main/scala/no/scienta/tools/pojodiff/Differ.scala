package no.scienta.tools.pojodiff

import scala.annotation.varargs

object Differ {

  def apply(objs: Iterable[AnyRef]): List[Mapper] = {
    val mappers: List[Mapper] = objs.toList map Mapper.apply
    val values: List[List[RefValue]] = fieldTypes flatMap mapperValues(mappers)
    val differences: List[List[RefValue]] = differingValues(values)

    turn90(differences) map Mapper.apply
  }

  private val fieldTypes = List(LeafField, MapField, ListField, NodeField)

  private def mapperValues(mappers: List[Mapper])(field: FieldType): List[List[RefValue]] =
    getRefValues(mappers, keys(field, mappers))

  private def getRefValues(mappers: List[Mapper], keys: List[RefKey]): List[List[RefValue]] =
    keys map (key => mappers map (_.refMap) map key)

  private def differingValues(values: List[List[RefValue]]): List[List[RefValue]] =
    values filter (_.toSet.size > 1)

  private def turn90[V](lists: List[List[V]]): List[List[V]] = {
    def reorgInternal(collectors: List[List[V]], originals: List[List[V]]): List[List[V]] = {
      if (originals exists (_.isEmpty)) collectors
      else reorgInternal(
        (originals map (_.head)) :: collectors,
        originals map (_.tail))
    }
    if (lists.isEmpty) Nil else reorgInternal(Nil, lists).reverse
  }

  private def keys(fieldType: FieldType, mappers: List[Mapper]): List[RefKey] =
    (mappers flatMap (_ subTree fieldType)).map(_._1).distinct
}

class Differ {

  import scala.collection.JavaConversions._

  @varargs def toDiffs(objs: AnyRef*): java.util.List[java.util.Map[String, AnyRef]] =
    Differ(objs.toList).map(_.toJava)
}
