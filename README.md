# LABORATORY:
## KAFKA / AVRO / SCALA / SCHEMA REGISTRY / SPECIFIC TYPE GENERATION

This project is very simple. The proposal is to be a sandbox to testing Kafka with Avro Serialized messages and Schema Registry validation.

At this point, there isn't anything special, but inside the project, you can generate Avro case classes and create easily  by Avro Schema or Arvro IDL maintaining the contract-first principle in this way.

## SBT MODULES

There are three modules

  - Model wich is the responsible of generate the model from AVRO.
  - Producer.
  - Consumer.

# Disclaimer

NestedRecord doesn't exist?. Don't care about the compilation now!. The feature point below it will explain to you how to generate this class

This sandbox need many components in order to use. The faster method that I recommend you is to use the Confluent Image Docker
__https://github.com/confluentinc/cp-docker-images/tree/5.2.1-post/examples/cp-all-in-one__

# Features!
  ### - Model
  You can generate the case clases from avsc or IDL. I'm using an sbt plugin sbt-avrohugger.
  Only thing thats you need is put your avro schema or IDL object into *model/src/main/avro*. After that you have three generation posibilities:
  1. **sbt model/avroScalaGenerate** -> Compiles the Avro files into Scala case classes.
  2. **sbt model/avroScalaGenerateSavro** -> Compiles the Avro files into Scala case class Scavro wrapper classes.
  3. **sbt model/avroScalaGenerateSpecific** -> Compiles the Avro files into Scala case classes implementing [SpecificRecord]

When you produce an Avro event in a specific topic in Confluent the schema will be registered automatically.

  You can also register an Avro Schema into Schema Registry with RegisterAvroSchema. The object expected three arguments.
The first one is the path of Avro Schema, second one is the URL of schema registry and the final argument is the topic where you want to register the avro schema.
```bash
sbt model/run avroSchema.avsc urlSchemaRegistry topic
```

# Run the Consumer

To run the consumer type as follows (Args optional are two: Kafka URL and Schema Registry URL)
```bash
sbt subscriber/run
```

# Run the producer

To run the producer you need type as follows (Args optional are three: Kafka URL, Schema Registry URL and the Topic)

```bash
sbt publisher/run
```


