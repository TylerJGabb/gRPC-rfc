import HelloWorldServer.logger
import contract.sdk.proto.Hello.{HelloRequest, HelloResponse, HelloWorldServiceGrpc}
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.{Server, ServerBuilder}

import java.util.logging.Logger
import scala.concurrent.{ExecutionContext, Future}

object HelloWorldServer {
  private val logger = Logger.getLogger(classOf[HelloWorldServer].getName)
  def main(args: Array[String]): Unit = {
    val server = new HelloWorldServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }
}

class HelloWorldServer(executionContext: ExecutionContext) { self =>
  var server: Server = _

  private def start(): Unit = {
    val serviceImpl: HelloWorldServiceGrpc.HelloWorldService = new ServiceImpl()
    val builder = ServerBuilder.forPort(50051)
    builder.addService(HelloWorldServiceGrpc.bindService(serviceImpl, executionContext))

    // enables api discovery via reflection
    // https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md
    builder.addService(ProtoReflectionService.newInstance())
    server = builder.build()
    server.start()
    logger.info(s"Server started, listening on 50051")
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down ***")
    }
  }

  private def stop(): Unit = {
    if(server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if(server != null) {
      server.awaitTermination()
    }
  }

  // https://scalapb.github.io/docs/grpc
  // https://github.com/xuwei-k/grpc-scala-sample/blob/master/grpc-scala/src/main/scala/io/grpc/examples/helloworld/HelloWorldServer.scala
  class ServiceImpl extends HelloWorldServiceGrpc.HelloWorldService {
    override def hello(request: HelloRequest): Future[HelloResponse] = {
      val reply = HelloResponse(responseText = s"hey! thanks for the message of '${request.requestText}'}")
      Future.successful(reply)
    }
  }

}

