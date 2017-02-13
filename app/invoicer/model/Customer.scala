package invoicer.model


import play.api.libs.json.Json
import reactivemongo.bson.Macros

case class Customer(firstName: String, lastName: String)

object Customer {
implicit val CustomerHandler = Macros.handler[Customer]
implicit val CustomerJson = Json.format[Customer]
}

