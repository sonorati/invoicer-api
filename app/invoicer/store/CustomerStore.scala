package invoicer.store

import db.Connector
import invoicer.model.Customer
import play.api.Logger
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomerStore {

  val collection: BSONCollection = Connector.collection("customers")

  def deleteCustomer(id: String): Unit = {
    Logger.info("Delete customer " + id)
    collection.remove(selector = BSONDocument("customerId" -> id))
  }

  def saveCustomer(customer: Customer): Unit = {
    Logger.info("Save customer")
    collection.insert(customer)
  }

  def findAllCustomers(): Future[List[Customer]] = {
    Logger.info(s"Find all customers")
    collection.find(BSONDocument())
      .cursor[Customer]()
      .collect[List]()
  }
}
