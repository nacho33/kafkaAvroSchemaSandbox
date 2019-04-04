package com.kafka.sandbox

import java.io.File

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import org.apache.avro.Schema

trait SchemaClient {

  val subject: String => String = topic => topic + "-value"
  val avroName: String = "t.avsc"

  def readRemoteAvroSchema(schemaRegistryUrl: String, topic: String): Schema = {
    println(s"Getting schema from Schema Registry: $schemaRegistryUrl to topic: $topic")
    val schemaReg = new CachedSchemaRegistryClient(schemaRegistryUrl, 100)
    val schemaMeta = schemaReg.getLatestSchemaMetadata(subject(topic))
    println(s"Schema latest Metadata ${schemaMeta.getId}")
    val schema: String = schemaMeta.getSchema
    println(s"schema to string $schema")
    new Schema.Parser().parse(schema)
  }

  def readLocalAvroSchema(avroName: String): Schema ={
    println(s"You are in $avroName")
    val schema: Schema = new org.apache.avro.Schema.Parser().parse(new File(s"$avroName"))
    schema
  }

  def registerSchema(schemaRegistryUrl: String, subject: String, avroName: String): Unit = {
    println(s"Schema: $schemaRegistryUrl subject: $subject and avroName: $avroName")
    val schema = readLocalAvroSchema(avroName)
    val client = new CachedSchemaRegistryClient(schemaRegistryUrl, 20)
    client.register(subject, schema)
  }
}
