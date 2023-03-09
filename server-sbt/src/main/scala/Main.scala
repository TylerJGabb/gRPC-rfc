import contract.sdk.proto.Hello.{HelloRequest, HelloResponse}

import scala.concurrent.Future

object Main {
  def main(args: Array[String]): Unit = {

  }
}

// https://scalapb.github.io/docs/grpc
// https://github.com/xuwei-k/grpc-scala-sample/blob/master/grpc-scala/src/main/scala/io/grpc/examples/helloworld/HelloWorldServer.scala
object ServerImpl extends contract.sdk.proto.Hello.HelloWorldServiceGrpc.HelloWorldService {
  override def hello(request: HelloRequest): Future[HelloResponse] = {
    val reply = HelloResponse(responseText = s"hey! thanks for the '${request.requestText}")
    Future.successful(reply)
  }
}