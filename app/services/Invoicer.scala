package services

import javax.inject.Inject

import invoicer.model.{Company, CompanyBasic, Invoice, PaymentDetails}
import invoicer.services.{CompanyService, InvoiceService}
import invoicer.store.InvoiceStore
import org.joda.time.DateTime

import scala.concurrent.Future

class Invoicer @Inject()(invoiceStore: InvoiceStore) {
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  def generateInvoice(daysWorked:Int, invoiceNumber:Int):String = {
    val company = new CompanyService
    val invoicer = new InvoiceService(invoiceNumber, company.makePayee(), company.makePayer())
    invoicer.generatePDF(daysWorked, invoiceNumber)
    s"Invoice $invoiceNumber generated"
  }

  def invoices(): Future[Seq[Invoice]] = {
    invoiceStore.findInvoices map { invoices =>
      invoices map { invoice =>
        Invoice(
          number = invoice.number,
          payee = Company(
            dailyRate = 333,
            basicData = CompanyBasic(name = "abc", address = "32", postCode = "3e"),
            vatNumber = "43",
            companyRegisteredNumber = "",
            paymentDetails = PaymentDetails(
              bank = "bank",
              accountNumber = 23444,
              sortCode = "430344"),
            description = "desc"),
          payer = null,
          daysWorked = 12,
          amountToPayPerDay = 20.0,
          date = DateTime.now(),
          percentage = 10.0)

      }
    }
  }

}
