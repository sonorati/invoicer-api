package db

import com.typesafe.config.ConfigFactory
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.Future

object Connector {

  import scala.concurrent.ExecutionContext.Implicits.global

  val config = ConfigFactory.load("persistence.conf")
  val mongoUri = config.getString("mongodb.uri")

  val driver = MongoDriver()

  val database: Future[DefaultDB] = for {
    parsedUri <- Future.fromTry(MongoConnection.parseURI(mongoUri))
    con = driver.connection(parsedUri)
    dn <- Future(parsedUri.db.get)
    db <- con.database(dn)
  } yield db

}
