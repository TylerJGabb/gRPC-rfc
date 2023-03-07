package client

import contract.sdk.proto.HelloWorldServiceGrpc.HelloWorldServiceBlockingStub
import contract.sdk.proto.Root
import contract.sdk.proto.Root.HelloRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	with(runApplication<Application>(*args)) {
		val proxy = getBean(HelloProxyService::class.java)
		proxy.sendRequest("12345")
	}
}

@Service
class HelloProxyService(
	@GrpcClient("myClient")
	val stub: HelloWorldServiceBlockingStub
) {

	fun sendRequest(string: String) {
		val request = with(HelloRequest.newBuilder()) {
			requestText = string
			build()
		}
		val result: Root.HelloResponse = stub.hello(request)
		println(result.responseText)
	}

}
