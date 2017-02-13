name := """invoicer-api"""

version := "0.0.2-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.xhtmlrenderer"   %   "flying-saucer-core" % "9.0.8",
  "org.xhtmlrenderer"   % "flying-saucer-pdf" % "9.0.8",
  "org.reactivemongo" % "reactivemongo_2.11" % "0.12.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

