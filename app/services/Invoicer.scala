package services

import javax.inject.Inject

import invoicer.model.Customer
import invoicer.services.{CompanyService, InvoiceService}
import invoicer.store.CustomerStore

import scala.concurrent.Future

class Invoicer @Inject()(customerStore: CustomerStore) {

  def generateInvoice(daysWorked:Int, invoiceNumber:Int):String = {
    val company = new CompanyService
    val invoicer = new InvoiceService(invoiceNumber, company.makePayee(), company.makePayer())
    invoicer.generatePDF(daysWorked, invoiceNumber)
    s"Invoice $invoiceNumber generated"
  }

  def deleteCustomer(id: String): Unit = {
    customerStore.deleteCustomer(id)
  }

  def saveCustomer(customer: Customer): Unit = {
    customerStore.saveCustomer(customer)
  }

  def findCustomers(): Future[List[Customer]] = {
    customerStore.findAllCustomers
  }
}
