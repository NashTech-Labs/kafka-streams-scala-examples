import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.knoldus",
      scalaVersion := "2.12.2",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "kafka-streams-scala-examples",
    libraryDependencies ++= Seq(
      kafkaStreams,
      scalaTest % Test
    )
  )
