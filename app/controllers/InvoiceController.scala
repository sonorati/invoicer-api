package controllers

import javax.inject._

import config.AppConfig
import play.api.libs.json._
import play.api.mvc._
import services.Invoicer
import views.InvoiceView

import scala.concurrent.Future
import scala.io.Source

@Singleton
class InvoiceController @Inject()(invoicer: Invoicer) extends Controller with AppConfig {

  import scala.concurrent.ExecutionContext.Implicits.global

  def generate(invoiceNumber:Int, daysWorked:Int) = Action {
    Ok(invoicer.generateInvoice(invoiceNumber, daysWorked))
  }

  def getInvoice(number:Int) = Action.async {
    val inputStream = Future(getClass.getClassLoader.getResourceAsStream("get-invoice-sample.json"))
    inputStream.map { is =>
      Ok(Source.fromInputStream(is).mkString)
    }
  }

  def invoices(companyId: String) = Action.async {
    invoicer.invoices() map { invoices =>
      Ok(Json.toJson(invoices)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> s"*")
    }
  }

}
