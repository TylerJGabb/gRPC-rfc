package sdk

import contract.sdk.proto.Root
import contract.sdk.proto.Root.HelloRequest
import io.grpc.stub.StreamObserver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

class Impl : contract.sdk.proto.HelloWorldServiceGrpc.HelloWorldServiceImplBase() {
	override fun hello(request: HelloRequest, responseObserver: StreamObserver<Root.HelloResponse>) {
		super.hello(request, responseObserver)

		responseObserver
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)

	val x: HelloRequest = with(HelloRequest.newBuilder()){
		requestText = "hello!"
		build()
	}
	println(x)

}
