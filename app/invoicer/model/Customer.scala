package invoicer.model

import play.api.libs.json.Json
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

case class Customer(val firstName: String, val lastName: String)

object Customer {
  implicit def customerReader = new BSONDocumentReader[Customer] {

    def read(doc: BSONDocument): Customer =
      new Customer(
        doc.getAs[String]("firstName").get,
        doc.getAs[String]("lastName").get
      )
  }

  implicit def customerWriter = new BSONDocumentWriter[Customer] {
    def write(customer: Customer): BSONDocument =
      BSONDocument(
        "_id" -> BSONObjectID.generate,
        "firstName" -> customer.firstName,
        "lastName" -> customer.lastName
      )
  }

  implicit val CustomerJson = Json.format[Customer]
}

