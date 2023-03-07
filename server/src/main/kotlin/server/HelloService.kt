package server

import contract.sdk.proto.HelloWorldServiceGrpc.HelloWorldServiceImplBase
import contract.sdk.proto.Root
import contract.sdk.proto.Root.HelloResponse
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.time.LocalDateTime


@GrpcService
class HelloService : HelloWorldServiceImplBase() {

    override fun hello(
        request: Root.HelloRequest,
        responseObserver: StreamObserver<HelloResponse>
    ) {
        val requestTime = DateTime.now(DateTimeZone.UTC)
        val response = with(HelloResponse.newBuilder()) {
            responseText = "Thank you for your request=${request.requestText} on ${requestTime}"
            build()
        }
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

}