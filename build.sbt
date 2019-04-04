import Deps._
import sbt.Keys.scalaVersion

lazy val root = (project in file(".")).
  aggregate(publisher, subscriber, model).
  settings(
    inThisBuild(List(
      organization := "com.kafka.sandbox",
      scalaVersion := "2.12.4",
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      resolvers += "io.confluent" at "http://packages.confluent.io/maven/"
    )),
    name := "scala_kafka_avro_laboratory"
  )

lazy val model = (project in file ("model")).
  settings(
    name := "model",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("com.kafka.sandbox.RegisterAvroSchema"),
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      scalaTest,
      scalaLogging,
      scallop)
  )

lazy val publisher = (project in file ("publisher")).
  settings(
    name := "publisher",
    mainClass := Some("com.kafka.sandbox.Producer"),
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      scalaLogging,
      scallop)
  ).dependsOn(model)

lazy val subscriber = (project in file ("subscriber")).
  settings(
    name := "subscriber",
    mainClass := Some("com.kafka.sandbox.Consumer"),
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      scalaLogging,
      scallop)
  ).dependsOn(publisher)


