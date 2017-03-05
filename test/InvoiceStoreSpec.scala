import invoicer.store.InvoiceStore
import invoicer.store.collections.{Company, InvoiceCollection}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.joda.time.DateTime
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider
import org.mongodb.scala.{Document, MongoClient}

import scala.concurrent.duration.Duration
import scala.language.implicitConversions

class InvoiceStoreSpec extends WithMongoDB {

  val INVOICER = "invoicer"
  private val WAIT_DURATION = Duration(10, "second")

  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[User], classOf[Contact], classOf[ Optional]),
    MongoClient.DEFAULT_CODEC_REGISTRY)

  "Macros" should "handle case classes" in withDatabase(INVOICER) {
    database =>
      val collection = database.getCollection[User]("user").withCodecRegistry(codecRegistry)

      val user = User(
        _id = 1,
        age = 30,
        username = "Bob",
        hobbies = List[String]("hiking", "music"),
        contacts = List(Contact("123 12314"), Contact("234 234234"))
      )
      collection.insertOne(user).futureValue

      info("The collection should have the expected document")
      val expectedDocument = Document(
        """{_id: 1, age: 30, username: "Bob", hobbies: ["hiking", "music"],
          | contacts: [{phone: "123 12314"}, {phone: "234 234234"}]}""".stripMargin)
      collection.find[Document]().first().futureValue.head should equal(expectedDocument)

      info("The collection should find and return the user")
      collection.find[User]().first().futureValue.head should equal(user)
  }

  "Invoice store" should "save and return list of invoices" in {

    val store = new InvoiceStore()
    val myCompany = Company(name = "Seon software", address = "greenwich", postCode = "se11xx")
    val invoice = InvoiceCollection(_id = 1, number = 456, company = myCompany, amount = 300.0, created = DateTime.now().getMillis)
    store.save(invoice).futureValue

    store.findInvoices().futureValue.head should equal(invoice)
  }
}
