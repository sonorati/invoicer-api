name := """invoicer-api"""

version := "0.0.2-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.xhtmlrenderer"   %   "flying-saucer-core" % "9.0.8",
  "org.xhtmlrenderer"   % "flying-saucer-pdf" % "9.0.8",
  "com.typesafe.play" %% "play-json" % "2.6.0-M1",
  "com.google.inject" % "guice" % "3.0",
  "org.mongodb.scala" % "mongo-scala-driver_2.12" % "1.2.2-4-g9b329b4-SNAPSHOT",
//  "org.mongodb.scala" % "mongo-scala-driver_2.12" % "1.2.1",
  "org.scalatestplus.play" % "scalatestplus-play_2.12" % "2.0.0-M2" % Test
)
