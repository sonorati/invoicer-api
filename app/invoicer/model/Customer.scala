package invoicer.model

import play.api.libs.json.Json
import reactivemongo.bson.{BSONObjectID, Macros}

case class Customer(customerId: String, firstName: String, lastName: String)

object Customer {BSONObjectID.generate()
  implicit val CustomerHandler = Macros.handler[Customer]
  implicit val CustomerJson = Json.format[Customer]
}

