import contract.sdk.proto.Hello.HelloWorldServiceGrpc.HelloWorldServiceBlockingStub
import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import contract.sdk.proto.Hello._

import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import scala.util.Try

object HelloWorldClient {
  def main(args: Array[String]): Unit = {
      val client = HelloWorldClient("localhost", 50051)
      client.sendRequest("1234109283740127893")
  }

  def apply(host: String, port: Int): HelloWorldClient = {
    val channelBuilder = ManagedChannelBuilder.forAddress(host, port)
    channelBuilder.usePlaintext()
    val channel = channelBuilder.build()
    val blockingStub = HelloWorldServiceGrpc.blockingStub(channel)
    new HelloWorldClient(channel, blockingStub)
  }

}

class HelloWorldClient private(
  private val channel: ManagedChannel,
  private val blockingStub: HelloWorldServiceBlockingStub
) {
  private[this] val logger = Logger.getLogger(classOf[HelloWorldClient].getName)

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def sendRequest(requestText: String): Try[Unit] = Try {
    logger.info(s"Sending request: $requestText")
    val request = HelloRequest(requestText)
    val response = blockingStub.hello(request)
    logger.info(s"Received response: ${response.responseText}")
  }
}