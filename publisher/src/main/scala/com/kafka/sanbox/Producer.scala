package com.kafka.sanbox

import java.time.Instant
import java.util.Properties

import com.kafka.sandbox.SchemaClient
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.rogach.scallop.ScallopConf

object Producer extends SchemaClient {

  private class Args(args: Array[String]) extends ScallopConf(args) {
    val kafkaUrl = opt[String](required = true, default = Some("http://localhost:9092"))
    val registryUrl = opt[String](required = true, default = Some("http://localhost:8081"))
    val topic = opt[String](required = true, default = Some("topic"))
    verify()
  }

  def main(args: Array[String]): Unit = {
    val cl = new Args(args)

    val props: Properties = new Properties()
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getCanonicalName)
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cl.kafkaUrl.toOption.get)
    props.put("schema.registry.url", cl.registryUrl.toOption.get)
    props.put("auto.register.schemas", false: java.lang.Boolean)

    implicit val producer = new KafkaProducer[String, NestedRecord](props)

    //    val schema = readRemoteAvroSchema(cl.registryUrl.toOption.get, cl.topic.toOption.get)

    //    val genericRecordFromPojo = AvroGenericRecordGenerator.mapObjectToGenericData(ObjectGenerator.generateObjectToSend).get
    //    println("Generic record from Pojo: " + genericRecordFromPojo.toString)
    //
    //    val genericRecordFromSR = AvroGenericRecordGenerator.mapObjectFromSchemaRegistryToGenericData(ObjectGenerator.generateObjectToSend, schema).get
    //    println("Generic record from SR: " + genericRecordFromSR.toString)

    for (i <- 1 to 1) {
      producer.send(new ProducerRecord(cl.topic.toOption.get, NestedRecord(Instant.now.getEpochSecond, SimpleRecord("Hello World!!!"))))
      println("publishing message " + i.toString)
    }

    producer.close()
  }

}
