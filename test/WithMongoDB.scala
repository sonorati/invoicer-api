import org.mongodb.scala.{MongoClient, MongoDatabase, Observable, Observer, Subscription}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success, Try}

trait WithMongoDB extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll {

  implicit val defaultPatience = PatienceConfig(timeout = Span(60, Seconds), interval = Span(5, Millis))
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  private var mongoDBOnline: Boolean = false
  private val DEFAULT_URI: String = "mongodb://localhost:27017/"
  private val DB_PREFIX = "mongo-scala-"
  private val WAIT_DURATION = Duration(10, "second")

  case class Contact(phone: String)
  case class User(_id: Int, username: String, age: Int, hobbies: List[String], contacts: List[Contact])
  case class Optional(_id: Int, optional: Option[Int])


  def isMongoDBOnline: Boolean = {
    Try(Await.result(MongoClient(DEFAULT_URI).listDatabaseNames(), Duration(5, "second"))).isSuccess
  }

  def mongoClient() = MongoClient(DEFAULT_URI)

  def checkMongoDB() {
    if (!isMongoDBOnline) {
      cancel("No Available Database")
    }
  }

  def withDatabase(dbName: String)(testCode: MongoDatabase => Any) {
    checkMongoDB()
    val client = mongoClient()
    val databaseName = "invoicer"
    val mongoDatabase = client.getDatabase(databaseName)
    try testCode(mongoDatabase) // "loan" the fixture to the test
    finally {
      // clean up the fixture
      Await.result(mongoDatabase.drop(), WAIT_DURATION)
      client.close()
    }
  }

  implicit def observableToFuture[TResult](observable: Observable[TResult]): Future[List[TResult]] = {
    val promise = Promise[List[TResult]]()
    class FetchingObserver extends Observer[TResult]() {
      val results = new ListBuffer[TResult]()

      override def onSubscribe(s: Subscription): Unit = {
        s.request(Int.MaxValue)
      }

      override def onError(t: Throwable): Unit = {
        promise.failure(t)
      }

      override def onComplete(): Unit = {
        promise.success(results.toList)
      }

      override def onNext(t: TResult): Unit = results.append(t)
    }
    observable.subscribe(new FetchingObserver())
    promise.future
  }

  implicit def observableToFutureConcept[T](Observable: Observable[T]): FutureConcept[List[T]] = {
    val future: Future[List[T]] = Observable
    new FutureConcept[List[T]] {
      def eitherValue: Option[Either[Throwable, List[T]]] = {
        future.value.map {
          case Success(o) => Right(o)
          case Failure(e) => Left(e)
        }
      }
      def isExpired: Boolean = false

      // Scala Futures themselves don't support the notion of a timeout
      def isCanceled: Boolean = false // Scala Futures don't seem to be cancelable either
    }
  }

}
