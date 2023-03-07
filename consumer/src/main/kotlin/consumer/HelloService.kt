package consumer

import contract.sdk.proto.HelloWorldServiceGrpc.HelloWorldServiceImplBase
import contract.sdk.proto.Root
import contract.sdk.proto.Root.HelloResponse
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService


@GrpcService
class HelloService : HelloWorldServiceImplBase() {

    override fun hello(
        request: Root.HelloRequest,
        responseObserver: StreamObserver<HelloResponse>
    ) {
        val response = with(HelloResponse.newBuilder()) {
            responseText = "Thank you for your request=${request.requestText}!"
            build()
        }
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

}