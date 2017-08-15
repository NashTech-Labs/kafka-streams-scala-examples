package com.knoldus.kafka.examples

import java.util.Properties

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.{KStreamBuilder, KeyValueMapper}

/**
  * Copyright Knoldus Software LLP, 2017. All rights reserved.
  */
object MapExampleWithSAM {
  def main(args: Array[String]): Unit = {
    val config = {
      val properties = new Properties()
      properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-application")
      properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
      properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
      properties
    }

    val stringSerde = Serdes.String()
    val integerSerde = Serdes.Integer()

    val builder = new KStreamBuilder()
    val originalStream = builder.stream("SourceTopic")

    //Works only with Scala 2.12.x
    val mappedStream =
      originalStream.map[String, Integer]((key: String, value: String) =>
        new KeyValue(key, new Integer(value.length))
      )
    mappedStream.to(stringSerde, integerSerde, "SinkTopic")

    val streams = new KafkaStreams(builder, config)
    streams.start()
  }
}
