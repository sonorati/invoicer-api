package invoicer.model

import play.api.libs.json.Json
import reactivemongo.bson.Macros

case class Customer(customerId: String, firstName: String, lastName: String)

object Customer {
  implicit val customerHandler = Macros.handler[Customer]
  implicit val customer = Json.format[Customer]
}

