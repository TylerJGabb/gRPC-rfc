import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import com.typesafe.config.ConfigFactory
import contract.sdk.proto.PiiServiceHandler

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

// https://developer.lightbend.com/guides/akka-grpc-quickstart-scala/
object PiiServer {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())

    val system = ActorSystem[Nothing](Behaviors.empty, "PiiServer", conf)
    new PiiServer(system).run()
  }
}

class PiiServer(system: ActorSystem[_]) {

  def run(): Future[Http.ServerBinding] = {
    implicit val sys = system
    implicit val ex: ExecutionContext = system.executionContext

    val service: HttpRequest => Future[HttpResponse] = {
      // https://doc.akka.io/docs/akka-grpc/current/server/reflection.html
      // server reflection is kind of like GQL Api Exploration, where the schema is provided
      // by the server to whoever wants to read it
      PiiServiceHandler.withServerReflection(new PiiServiceImpl(system))
    }

    val bound: Future[Http.ServerBinding] = Http(system)
      .newServerAt(interface = "127.0.0.1", port = 50052)
      .bind(service)
      .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))

    bound.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        println(s"gRPC server bound to ${address.getHostString}:${address.getPort}")
      case Failure(exception) =>
        println("Failed to bind gRPC endpoint, terminating system", exception)
        system.terminate()
    }

    bound
  }
}
