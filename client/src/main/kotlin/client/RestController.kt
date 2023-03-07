package client

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("grpc-proxy")
class RestController(
    val grpcProxy: GrpcProxy
) {

    @GetMapping("/{msg}")
    fun grpcProxy(@PathVariable msg: String): String {
        return grpcProxy.proxyRequest(msg)
    }

}