package invoicer.config

import java.io.InputStream
import java.nio.file.Files._
import java.nio.file.{Files, Paths}

import com.typesafe.config.{Config, ConfigFactory}

import scala.io.Source._

trait AppConfig {

  lazy val externalPath = appConfig.getString("pdf.external.files")

  lazy val hasExternalConfig = Files.exists(Paths.get(externalPath))
  lazy val fileNamePrefix = config.getString("pdf.name.prefix")
  lazy val fileNameSuffix = config.getString("pdf.name.suffix")
  lazy val pdfPath = config.getString("pdf.location")

  def config: Config = {
    if (hasExternalConfig) {
      ConfigFactory.parseString(fromFile(externalPath+"pdf-details.properties").mkString)
    } else ConfigFactory.load("pdf-details.properties")
  }

  val appConfig:Config = ConfigFactory.load("config.properties")

  def loadResource(file: String): InputStream = {
    if(hasExternalConfig) newInputStream(Paths.get(externalPath+file))
     else getClass.getClassLoader.getResourceAsStream(file)
  }
}
