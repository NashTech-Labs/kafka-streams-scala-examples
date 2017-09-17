package com.knoldus.kafka.examples

import java.util.Properties

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStreamBuilder, KeyValueMapper, ValueMapper}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import scala.collection.JavaConverters._

/**
  * Copyright Knoldus Software LLP, 2017. All rights reserved.
  */
object AggregationExample {
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

    val mappedStream =
      originalStream.flatMapValues[String] {
        new ValueMapper[String, java.lang.Iterable[java.lang.String]]() {
          override def apply(value: String): java.lang.Iterable[java.lang.String] = {
            value.toLowerCase.split("\\W+").toIterable.asJava
          }
        }
      }.groupBy {
        new KeyValueMapper[String, String, String]() {
          override def apply(key: String, word: String): String = word
        }
      }.count("Counts")
    mappedStream.to(stringSerde, longSerde, "SinkTopic")

    val streams = new KafkaStreams(builder, config)
    streams.start()
  }
}
