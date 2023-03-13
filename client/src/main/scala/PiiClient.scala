import akka.{Done, NotUsed}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import contract.sdk.proto.{PiiRequest, PiiResponse, PiiServiceClient}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


object PiiClient {

  val token = "fake_token"

  def main(args: Array[String]): Unit = {
    implicit val sys: ActorSystem[_] = ActorSystem(Behaviors.empty, "PiiClient")
    implicit val ex: ExecutionContext = sys.executionContext

    val client = PiiServiceClient(GrpcClientSettings.fromConfig("piiServiceClient"))

    val queries = Seq("Q1", "Q2")

    queries.foreach(singleRequestReply)
    Thread.sleep(10000)
    queries.foreach(streamingBroadcast)

    def singleRequestReply(query: String): Unit = {
      println(s"Performing single request-reply: $query")
      val reply = client.executePiiQuery(PiiRequest(token, query))
      reply.onComplete {
        case Success(piiResponse) =>
          println(piiResponse.queryResult)
        case Failure(exception) =>
          println(s"Error: $exception")
      }
    }

    def streamingBroadcast(query: String): Unit = {
      println(s"Performing streaming requests: $query")

      val requestStream: Source[PiiRequest, NotUsed] =
        Source
          .tick(1.second, 1.second, "tick")
          .zipWithIndex
          .map { case (_, i) => i }
          .map(i => PiiRequest(s"$query-$i"))
          .mapMaterializedValue(_ => NotUsed)

      val responseStream: Source[PiiResponse, NotUsed] = client.streamPiiQuery(requestStream)
      val done: Future[Done] =
        responseStream.runForeach(reply => println(s"$query for streaming reply: ${reply.queryResult}"))

      done.onComplete {
        case Success(_) =>
          println("streaming done")
        case Failure(e) =>
          println(s"Error streaming: $e")
      }
    }
  }
}
