package invoicer.store.collections

case class Optional(_id: Int, optional: Option[Int])
case class Company(name: String,
                   address: String,
                   postCode: String)

case class InvoiceCollection(_id: Int, number: Int, amount: Double, created: Long, company: Company)
