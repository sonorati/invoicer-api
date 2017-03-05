package services

import javax.inject.Inject

import invoicer.services.{CompanyService, InvoiceService}
import invoicer.store.InvoiceStore
import org.joda.time.DateTime
import views.InvoiceView

import scala.concurrent.Future

class Invoicer @Inject()(invoiceStore: InvoiceStore) {
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  def generateInvoice(daysWorked:Int, invoiceNumber:Int):String = {
    val company = new CompanyService
    val invoicer = new InvoiceService(invoiceNumber, company.makePayee(), company.makePayer())
    invoicer.generatePDF(daysWorked, invoiceNumber)
    s"Invoice $invoiceNumber generated"
  }

  def invoices(): Future[Seq[InvoiceView]] = {
    invoiceStore.findInvoices map { invoices =>
      invoices map { invoice =>
        InvoiceView(
          number = invoice.number,
          date = new DateTime(invoice.created),
          amount = invoice.amount)
      }
    }
  }

}
