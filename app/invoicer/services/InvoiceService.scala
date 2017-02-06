package invoicer.services

import java.nio.file.{Files, Paths}

import invoicer.config.AppConfig
import invoicer.model.{Company, CompanyBasic, Invoice}

class InvoiceService(invoiceNumber:Int, payee:Company, payer:CompanyBasic) extends AppConfig {

  def generatePDF(daysWorked:Int, invoiceNumber:Int) {
    val pdfName = makePdfName(invoiceNumber)
    val invoice = Invoice(invoiceNumber,payee, payer, daysWorked, payee.dailyRate)
    val htmlInvoice = HtmlBuilder.makeHtmlInvoice(invoice)
    print(s"GENERATING PDF WITH NAME: $pdfName")
    PdfBuilder.generatePdf(htmlInvoice, pdfName)
  }

  private def makePdfName(invoiceNumber:Int):String = {
    if (Files.exists(Paths.get(pdfPath)))
      s"$pdfPath/$fileNamePrefix$invoiceNumber $fileNameSuffix"
    else s"$fileNamePrefix$invoiceNumber $fileNameSuffix"
  }
}
