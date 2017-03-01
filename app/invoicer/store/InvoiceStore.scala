package invoicer.store

import db.Connector
import invoicer.model.Invoice
import play.api.Logger
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InvoiceStore {
  val collection: BSONCollection = Connector.collection("invoices")

  def invoices(): Future[List[Invoice]] = {
    Logger.info(s"Find all invoices")
    collection.find(BSONDocument())
      .cursor[Invoice]()
      .collect[List]()
  }

}
