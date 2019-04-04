package com.kafka.sandbox

import java.util
import java.util.Properties

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import org.rogach.scallop.ScallopConf

object Consumer {
  private class Args(args: Array[String]) extends ScallopConf(args) {
    val kafkaUrl = opt[String](required = true, default = Some("http://localhost:9092"))
    val registryUrl = opt[String](required = true, default = Some("http://localhost:8081"))
    verify()
  }

  def main(args: Array[String]): Unit = {
    val args = new Args(args)

    val consumerProps: Properties = new Properties()

    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getCanonicalName)
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, args.kafkaUrl.toOption.get)
    consumerProps.put("schema.registry.url", args.registryUrl.toOption.get)
    consumerProps.put("group.id", "consumer-group")
    consumerProps.put("specific.avro.reader", "true")

    val consumer = new  KafkaConsumer[String, GenericRecord](consumerProps)

    Runtime.getRuntime.addShutdownHook(new Thread(() => consumer.close()))

    consumer.subscribe(util.Arrays.asList("topic"))

    while(true) {
      val records = consumer.poll(2000)
      records.forEach(record => {
        println(record.value().toString)
        println("received message: " + record.toString)
      })
      consumer.commitSync()
    }
  }
}
