package invoicer.services

import java.text.DecimalFormat

import invoicer.model.Invoice

import scala.xml.Elem


object HtmlBuilder {

  def makeHtmlInvoice(invoice:Invoice) = {
    val formatter = new DecimalFormat("#,###.00")
    val elem: Elem = <html>
      <body>
        <p align="right" width="16%" style="font-size:medium;font-family:sans-serif">Invoice No. SC00000000{invoice.number}</p>
        <p style="font-size:20px;font-family:sans-serif">{invoice.payee.basicData.name}</p>

        <p style="font-size:11px;font-family:sans-serif;margin-top:-1.5em;vertical-align=top">{invoice.payee.basicData.address} Postcode {invoice.payee.basicData.postCode}</p>
        <hr style="border-style=double" />
        <p style="font-size:14px;font-family:sans-serif" align="right"> Date {invoice.prettyDate}</p>
        <div width="100%">
          <table width="60%" style="font-family:sans-serif;border:1px solid black;border-radius:10px;-moz-border-radius:10px">
            <tr>
              <td align="left">
                <table width="100%">
                  <td><b>customer</b></td>
                  <tr>
                    <td><p style="font-size:14px">Name {invoice.payer.name}</p></td>
                  </tr>
                  <tr>
                    <td><p style="font-size:14px">Address    {invoice.payer.address}</p></td>
                  </tr>
                  <tr>
                    <td><p style="font-size:14px">Postcode   {invoice.payer.postCode}</p></td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
          <br/>
        </div>
        <table style="width:100%;border-collapse:collapse;text-align:center;font-size:14px;font-family:sans-serif;border:dotted;border-width:thin" border="1|0">
          <tr>
            <td><b>Qty</b></td>
            <td><b>Description</b></td>
            <td><b>Unit Price</b></td>
            <td><b>Total</b></td>
          </tr>

          <tr style="height:230px;vertical-align:top">
            <td>{invoice.daysWorked}</td>
            <td style="text-align:left">{invoice.payee.description}</td>
            <td>£{formatter.format(invoice.amountToPayPerDay)}</td>
            <td>£{formatter.format(invoice.totalWithoutVat)}</td>
          </tr>
        </table>
        <table width="100%">
          <tr>
            <td width="70%">
              <table align="left" width="300px" style="padding:1em;margin-top:1em;font-size:12px;font-family:sans-serif;border:5px solid black;border-width:thin;border-radius:10px;-moz-border-radius:10px;-webkit-border-radius:10px;">
                <tr>
                  <td><b>Payment Details</b></td>
                </tr>
                <tr style="font-size:10px">
                  <td>Please pay via BACS transfer to:</td>
                </tr>
                <tr>
                  <td>{invoice.payee.paymentDetails.bank}</td>
                </tr>
                <tr>
                  <td>Acc. Number: {invoice.payee.paymentDetails.accountNumber}</td>
                </tr>
                <tr>
                  <td>Sortcode: {invoice.payee.paymentDetails.sortCode}</td>
                </tr>
              </table>
            </td>
            <td style="vertical-align:top">
              <table width="100%" style="font-size:14px;font-family:sans-serif" align="right">
                <tr>
                  <td>SubTotal</td><td>   £{formatter.format(invoice.totalWithoutVat)}</td>
                </tr>
                <tr>
                  <td> VAT</td><td>     £{formatter.format(invoice.totalVat)}</td>
                </tr>
                <tr>
                  <td><b>TOTAL</b></td><td>  £{formatter.format(invoice.total)}</td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <p align="center" style="font-size:12px"> VAT No: {invoice.payee.vatNumber}</p>
        <hr/>
        <p align="center" style="font-size:12px;font-style:italic">
          Company registered in England and Wales No: ({invoice.payee.companyRegisteredNumber})
        </p>
      </body>
    </html>
    elem
  }
}
