package controllers

import javax.inject._

import play.api.mvc._
import services.Invoicer

/**
 * This controller demonstrates how to use dependency injection to
 * bind a component into a controller class. The class creates an
 * `Action` that shows an incrementing count to users. The
 * object is injected by the Guice dependency injection system.
 */
@Singleton
class InvoiceController @Inject()(invoicer: Invoicer) extends Controller {

  /**
   * Create an action that responds with the 's current
   * count. The result is plain text. This `Action` is mapped to
   * `GET /count` requests by an entry in the `routes` config file.
   */
  def generate(invoiceNumber:Int, daysWorked:Int) = Action {
    Ok(invoicer.generateInvoice(invoiceNumber, daysWorked))
  }

  def sayHi() = Action {
    Ok("Here it is!")
  }
}
