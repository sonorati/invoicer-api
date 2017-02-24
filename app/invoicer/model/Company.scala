package invoicer.model

import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.Json
import reactivemongo.bson.{BSONDateTime, BSONHandler, Macros}

case class Company(
                    dailyRate: Int,
                    basicData: CompanyBasic,
                    vatNumber: String,
                    companyRegisteredNumber: String,
                    paymentDetails: PaymentDetails,
                    description: String
                    )

case class CompanyBasic(name: String,
                        address: String,
                        postCode: String)

case class PaymentDetails(
                           bank: String,
                           accountNumber: Int,
                           sortCode: String
                           )
case class Invoice(
                    number: Int,
                    payee: Company,
                    payer: CompanyBasic,
                    daysWorked: Int,
                    amountToPayPerDay: Double,
                    date: DateTime = DateTime.now,
                    percentage: Double = 20
                    ) {

  val vat: Double = amountToPayPerDay * (percentage / 100)
  val totalWithoutVat = amountToPayPerDay * daysWorked
  val totalVat = vat * daysWorked
  val total: Double = totalWithoutVat + totalVat
  val prettyDate: String = s"${date.dayOfMonth()}/${date.monthOfYear()}/${date.year}"
}

object Invoice {
  DateTimeZone.setDefault(DateTimeZone.UTC)
  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    val fmt = ISODateTimeFormat.dateTime()
    def read(time: BSONDateTime) = new DateTime(time.value)
    def write(jdtime: DateTime) = BSONDateTime(jdtime.getMillis)
  }
  implicit val paymentDetailsHandler = Macros.handler[PaymentDetails]
  implicit val companyBasicHandler = Macros.handler[CompanyBasic]
  implicit val companyHandler = Macros.handler[Company]
  implicit val invoiceHandler = Macros.handler[Invoice]
  implicit val companyBasic = Json.format[CompanyBasic]
  implicit val paymentDetails = Json.format[PaymentDetails]
  implicit val company = Json.format[Company]
  implicit val invoice = Json.format[Invoice]
}

object Company {
  implicit val companyBasic = Json.format[CompanyBasic]
  implicit val paymentDetails = Json.format[PaymentDetails]
  implicit val company = Json.format[Company]
}

object CompanyBasic {
  implicit val companyBasic = Json.format[CompanyBasic]
  implicit val paymentDetails = Json.format[PaymentDetails]
  implicit val company = Json.format[Company]
}
