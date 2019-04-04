package com.kafka.sandbox

import org.rogach.scallop.{ScallopConf, ScallopOption}

object RegisterAvroSchema extends SchemaClient {

  private class Args(args: Array[String]) extends ScallopConf(args) {
    val avroFile: ScallopOption[String] = opt[String](required = true, default = Some("avroSchema.avsc"))
    val registryUrl: ScallopOption[String] = opt[String](required = true, default = Some("http://localhost:8081"))
    val topic: ScallopOption[String] = opt[String](required = true, default = Some("topic"))
    verify()
  }

  def main(args: Array[String]): Unit = {
    val cl = new Args(args)
    println(s"Registering Avro Schema to ${cl.registryUrl.toOption.get}" )
    println(s"Avro File to register into Schema is ${cl.avroFile.toOption.get}" )
    println(s"Topic is ${cl.topic.toOption.get}" )

    registerSchema(cl.registryUrl.toOption.get, subject( cl.topic.toOption.get), cl.avroFile.toOption.get)


  }
}
