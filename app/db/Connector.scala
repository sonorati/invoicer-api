package db

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

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


  def collection(name: String): BSONCollection = Await.result(database.map(_.collection(name)), Duration(30, TimeUnit.SECONDS))

}
