package controllers

import javax.inject._

import config.AppConfig
import invoicer.model.Customer
import play.api.libs.json.Json
import play.api.mvc._
import services.Invoicer

import scala.concurrent.Future
import scala.io.Source

@Singleton
class InvoiceController @Inject()(invoicer: Invoicer) extends Controller with AppConfig {

  import scala.concurrent.ExecutionContext.Implicits.global

  def generate(invoiceNumber:Int, daysWorked:Int) = Action {
    Ok(invoicer.generateInvoice(invoiceNumber, daysWorked))
  }

  def getInvoice(number:Int) = Action {
    val is = getClass.getClassLoader.getResourceAsStream("get-invoice-sample.json")
    Ok(Source.fromInputStream(is).mkString)
  }

  def saveCustomer() = Action {request =>
    val json = request.body.asJson.get
    val customer = json.as[Customer]
    invoicer.saveCustomer(customer)
    Created
  }

  def findCustomers = Action.async {
    val customers: Future[List[Customer]] = invoicer.findCustomers()
    customers.map { seq =>
      Ok(Json.toJson(seq))
    }
  }

  def sayHi() = Action {
    Ok("Here it is!")
  }
}
