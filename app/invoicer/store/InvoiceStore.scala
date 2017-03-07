package invoicer.store

import db.DBConnector
import invoicer.store.collections.InvoiceCollection
import invoicer.store.collections.InvoiceCollection.codecRegistry
import org.mongodb.scala.{Completed, Observable}

import scala.concurrent.Future

class InvoiceStore {

  private val COLLECTION_NAME = "invoice"
  val collection = DBConnector.db.getCollection[InvoiceCollection](COLLECTION_NAME).withCodecRegistry(codecRegistry)

  def findInvoices(): Future[Seq[InvoiceCollection]] = {
    collection.find[InvoiceCollection]().toFuture()
  }

  def save(invoice: InvoiceCollection): Observable[Completed] = {
    collection.insertOne(invoice)
  }

}
