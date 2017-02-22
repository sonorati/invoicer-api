package controllers

import javax.inject._

import config.AppConfig
import invoicer.model.Customer
import play.api.libs.json._
import play.api.mvc._
import services.Invoicer

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

  def deleteCustomer(id:String) = Action {
    invoicer.deleteCustomer(id)
    Ok.withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> s"*")
  }

  def saveCustomer = Action { request =>
    val json = request.body.asJson.get
    val customer = jsonWithRandomId(json).as[Customer]
    invoicer.saveCustomer(customer)
    Created.withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> s"*")
  }

  def findCustomers = Action.async {
    invoicer.findCustomers map { customers =>
      Ok(Json.toJson(customers)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> s"*")
    }
  }

  def options(path: String) = Action {
    Ok("").withHeaders(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
    "Access-Control-Allow-Credentials" -> "true",
    "Access-Control-Max-Age" -> (60 * 60 * 24).toString
    )
  }

  def sayHi() = Action {
    Ok("Here it is!")
  }

  private def jsonWithRandomId(json: JsValue): JsObject = {
    val customerId: String = scala.util.Random.nextLong().toString
    json.as[JsObject] + ("customerId" -> Json.toJson(customerId))
  }

}
