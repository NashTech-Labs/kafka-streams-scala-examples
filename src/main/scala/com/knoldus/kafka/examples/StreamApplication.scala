package com.knoldus.kafka.examples

import java.util.Properties

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.KStreamBuilder

/**
  * Copyright Knoldus Software LLP, 2017. All rights reserved.
  */
object StreamApplication {
  def main(args: Array[String]): Unit = {
    val config = {
      val properties = new Properties()
      properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-application")
      properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
      properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
      properties
    }

    val builder = new KStreamBuilder()
    val sourceStream = builder.stream("SourceTopic")
    sourceStream.to("SinkTopic")

    val streams = new KafkaStreams(builder, config)
    streams.start()
  }
}
