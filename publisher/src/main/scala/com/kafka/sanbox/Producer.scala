package com.kafka.sanbox

import java.time.Instant
import java.util.Properties

import com.kafka.sandbox.SchemaClient
import com.kafka.sandbox.event.{NestedRecord, SimpleRecord}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.rogach.scallop.ScallopConf

import scala.io.Source

object Producer extends SchemaClient {

  private class Args(args: Array[String]) extends ScallopConf(args) {

    verify()
  }

  def main(args: Array[String]): Unit = {
    val cl = new Args(args)

    val props: Properties = new Properties()
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cl.kafkaUrl.toOption.get)
//    props.put("schema.registry.url", cl.registryUrl.toOption.get)
//    props.put("auto.register.schemas", false: java.lang.Boolean)

    implicit val producer = new KafkaProducer[String, String](props)

    // File with events to produce
    val events = Source.fromFile("/home/icasado/projects/kafkaAvroSchemaSandbox/publisher/src/main/resources/casos.txt").getLines().toList

    //    val schema = readRemoteAvroSchema(cl.registryUrl.toOption.get, cl.topic.toOption.get)

    //    val genericRecordFromPojo = AvroGenericRecordGenerator.mapObjectToGenericData(ObjectGenerator.generateObjectToSend).get
    //    println("Generic record from Pojo: " + genericRecordFromPojo.toString)
    //
    //    val genericRecordFromSR = AvroGenericRecordGenerator.mapObjectFromSchemaRegistryToGenericData(ObjectGenerator.generateObjectToSend, schema).get
    //    println("Generic record from SR: " + genericRecordFromSR.toString)

//    for (i <- 1 to 1) {
//      producer.send(new ProducerRecord(cl.topic.toOption.get, NestedRecord(Instant.now.getEpochSecond, SimpleRecord("Hello World!!!"))))
//      println("publishing message " + i.toString)
//    }

    events.foreach(event => {
      val value = producer.send(new ProducerRecord(cl.topic.toOption.get, event))
    //  println(s"Produce -> $event and metadata offset: ${value.get().offset()} partition ${value.get().partition()}")
    })

    println(s"Published into ${cl.kafkaUrl.toOption.get} to topic ${cl.topic.toOption.get} ....")

    producer.close()
  }

}
