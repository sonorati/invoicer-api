package services

import javax.inject.Inject

import invoicer.model.Invoice
import invoicer.services.{CompanyService, InvoiceService}
import invoicer.store.InvoiceStore

import scala.concurrent.Future

class Invoicer @Inject()(invoiceStore: InvoiceStore) {

  def generateInvoice(daysWorked:Int, invoiceNumber:Int):String = {
    val company = new CompanyService
    val invoicer = new InvoiceService(invoiceNumber, company.makePayee(), company.makePayer())
    invoicer.generatePDF(daysWorked, invoiceNumber)
    s"Invoice $invoiceNumber generated"
  }

  def invoices(): Future[List[Invoice]] = invoiceStore.invoices()

}
