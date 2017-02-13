package invoicer.store

import db.Connector
import invoicer.model.Customer
import play.api.Logger
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomerStore {

  def customers: Future[BSONCollection] =
    Connector.database.map(_.collection("customers"))

  def saveCustomer(customer: Customer): Unit = {
    Logger.info(s"save customer")
    customers flatMap (_.insert(customer))
  }

  def findAllCustomers(): Future[List[Customer]] = {
    Logger.info(s"Find all customers")

    customers flatMap (_.
      find(BSONDocument()).
      cursor[Customer]().
      collect[List]())
  }

}
