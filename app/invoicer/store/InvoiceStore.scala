package invoicer.store

import db.DBConnector
import invoicer.store.collections.{Company, InvoiceCollection, Optional}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider
import org.mongodb.scala.{Completed, MongoClient, Observable}

import scala.concurrent.Future

class InvoiceStore {
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[InvoiceCollection], classOf[Company], classOf[ Optional]),
    MongoClient.DEFAULT_CODEC_REGISTRY)
  val collection = DBConnector.db.getCollection[InvoiceCollection]("invoice").withCodecRegistry(codecRegistry)

  def findInvoices(): Future[Seq[InvoiceCollection]] = {
    collection.find[InvoiceCollection]().toFuture()
  }

  def save(invoice: InvoiceCollection): Observable[Completed] = {
    collection.insertOne(invoice)
  }

}
