package com.kafka.sandbox

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.reflect.ReflectData

object AvroGenericRecordGenerator {

  def mapObjectToGenericData(objToMap: Object): Option[GenericRecord] = {
    val schema = ReflectData.get().getSchema(objToMap.getClass)
    makeGenericRecordFromObject(objToMap, schema)
  }

  def mapObjectFromSchemaRegistryToGenericData(objToMap: Object, schema: Schema): Option[GenericData.Record] =
    makeGenericRecordFromObject(objToMap, schema)

  private def makeGenericRecordFromObject(objToMap: Object, schema: Schema): Option[GenericData.Record] = {
   Some((objToMap.getClass.getDeclaredFields foldLeft new GenericData.Record(schema))((record, field) => {
      field.setAccessible(true)
      record.put(field.getName, field.get(objToMap))
      record
    }))
  }
}
