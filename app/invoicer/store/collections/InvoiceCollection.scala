package invoicer.store.collections

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider

case class Optional(_id: Int, optional: Option[Int])
case class Company(name: String,
                   address: String,
                   postCode: String)

case class InvoiceCollection(_id: Int, number: Int, amount: Double, created: Long, company: Company)

object InvoiceCollection {
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[InvoiceCollection], classOf[Company], classOf[ Optional]),
    MongoClient.DEFAULT_CODEC_REGISTRY)
}
