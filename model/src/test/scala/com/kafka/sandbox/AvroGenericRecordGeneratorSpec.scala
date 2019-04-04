package com.kafka.sandbox

import org.apache.avro.generic.GenericRecord
import org.scalatest.{Matchers, WordSpecLike}

case class SimpleObject(data1: String, data2: String)

class AvroGenericRecordGeneratorSpec extends WordSpecLike with Matchers {

  "The avro generic record generator getting SimpleObject" should {
    "return return us the simple object" in {
      val genericRecord: Option[GenericRecord] = AvroGenericRecordGenerator.mapObjectToGenericData(SimpleObject("1", "2"))
      genericRecord.get.get("data1") should be("1")
      genericRecord.get.get("data2") should be("2")
      println(genericRecord.toString)
    }
  }

}
