package db

import org.mongodb.scala.MongoClient


object DBConnector {

  private val DEFAULT_URI: String = "mongodb://localhost:27017/"

  private val databaseName =  "invoicer"
  def mongoClient() = MongoClient(DEFAULT_URI)

  val client = mongoClient()
  val db = client.getDatabase(databaseName)

}
