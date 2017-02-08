package controllers

import javax.inject._

import config.AppConfig
import play.api.mvc._
import services.Invoicer

import scala.io.Source

@Singleton
class InvoiceController @Inject()(invoicer: Invoicer) extends Controller with AppConfig{

  def generate(invoiceNumber:Int, daysWorked:Int) = Action {
    Ok(invoicer.generateInvoice(invoiceNumber, daysWorked))
  }

  def getInvoice(number:Int) = Action {
    val is = getClass.getClassLoader.getResourceAsStream("get-invoice-sample.json")
    Ok(Source.fromInputStream(is).mkString)
  }
  def sayHi() = Action {
    Ok("Here it is!")
  }
}
