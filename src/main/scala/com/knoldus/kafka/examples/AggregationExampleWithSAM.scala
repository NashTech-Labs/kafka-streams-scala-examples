package com.knoldus.kafka.examples

import java.util.Properties

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import scala.collection.JavaConverters._

/**
  * Copyright Knoldus Software LLP, 2017. All rights reserved.
  */
object AggregationExampleWithSAM {
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
    val longSerde = Serdes.Long()

    val builder = new KStreamBuilder()
    val originalStream = builder.stream("SourceTopic")

    val mappedStream: KTable[String, java.lang.Long] =
      originalStream.flatMapValues((value: String) =>
        value.toLowerCase.split("\\W+").toIterable.asJava)
        .groupBy((_, word) => word)
        .count("Counts")
    mappedStream.to(stringSerde, longSerde, "SinkTopic")

    val streams = new KafkaStreams(builder, config)
    streams.start()
  }
}
