package invoicer.services

import java.io.{File, FileWriter}

import org.xhtmlrenderer.simple.PDFRenderer

import scala.xml.Elem

object PdfBuilder {

  def generatePdf(html: Elem, pdfName:String): Unit = {
    val file = new File("htmlFile")
    val fileW = new FileWriter(file)
    fileW.write(html.mkString)
    fileW.close()

    PDFRenderer.renderToPDF(file, pdfName)
    file.delete()
  }
}
