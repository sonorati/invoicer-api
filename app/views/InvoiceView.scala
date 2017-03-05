package views

import org.joda.time.DateTime
import play.api.libs.json.Json


case class InvoiceView(number: Int, date:DateTime, amount:Double)


object InvoiceView {
  implicit val invoice = Json.format[InvoiceView]
}
