import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val kafkaStreams = "org.apache.kafka" % "kafka-streams" % "0.11.0.0"
}
