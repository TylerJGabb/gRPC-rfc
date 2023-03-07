package client

import contract.sdk.proto.HelloWorldServiceGrpc
import contract.sdk.proto.Root
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class GrpcProxy(
    @GrpcClient("myClient")
    val stub: HelloWorldServiceGrpc.HelloWorldServiceBlockingStub
) {

    fun proxyRequest(string: String): String {
        val request = with(Root.HelloRequest.newBuilder()) {
            requestText = string
            build()
        }
        val result: Root.HelloResponse = stub.hello(request)
        return result.responseText
    }
}