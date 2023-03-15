import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import contract.sdk.proto.{PiiRequest, PiiResponse, PiiServiceClient}

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContext, Future}


object PiiClient extends App {

  val token = "fake_token"

  implicit val sys: ActorSystem[_] = ActorSystem(Behaviors.empty, "PiiClient")
  implicit val ex: ExecutionContext = sys.executionContext

  val restPort = sys.settings.config.getInt("rest.api.port")
  val grpcClient = PiiServiceClient(GrpcClientSettings.fromConfig("piiServiceClient"))

  // https://doc.akka.io/docs/akka-http/current/routing-dsl/index.html#longer-example
  def topLevelRoute: Route =
    concat(
      path("single-request" / Remaining)(singleRequestRoute),
      path("start-streaming" / Remaining)(startStreamingRoute),
    )

  def singleRequestRoute(query: String): Route = get {
    println(s"Performing single request-reply: $query")
    val executeFuture: Future[PiiResponse] = grpcClient.executePiiQuery(PiiRequest(token, query))
    val response: PiiResponse = Await.result(executeFuture, 10.seconds)
    complete(s"response: ${response.queryResult}, label=${response.bigqueryLabel}")
  }

  def startStreamingRoute(query: String): Route = get {
    println(s"Performing streaming request for query: $query")
    val tickingSource = Source.tick(1.second, 1.second, "tick")
      .zipWithIndex
      .take(10)
      .map { case (_, i) => i }
      .map(i => PiiRequest(token, s"$query - Part_$i"))
      .mapMaterializedValue(_ => NotUsed)


    val responseStream: Source[PiiResponse, NotUsed] = grpcClient.streamPiiQuery(tickingSource)
    val concatSink = Sink.fold[String, String]("")(_ + "\n" + _)
    val foldedResponses: RunnableGraph[Future[String]] = responseStream
      .take(10)
      .map(_.queryResult)
      .toMat(concatSink)(Keep.right)


    val result = Await.result(foldedResponses.run(), 30.seconds)
    complete(result)
  }

  val bindingFuture = Http().newServerAt("0.0.0.0", restPort).bind(topLevelRoute)
  println(s"REST server now online, listening on 0.0.0.0:$restPort")
  Await.result(bindingFuture, Duration.Inf)
}
