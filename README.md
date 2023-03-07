this is a sandbox to demonstrate using gRPC for service-to-service comms

- https://github.com/yidongnan/grpc-spring-boot-starter
- https://yidongnan.github.io/grpc-spring-boot-starter/en/trouble-shooting.html

# How to run this demo
1. inside `server/` directory run `./gradlew bootRun`. you will see a gRPC server start listening on 9090
2. inside `client/` directory run `./gradlew bootRun`. you will see a REST server start listening on 8080
3. use your favorite client to send a get request to `localhost:8080/grpc-proxy/abc`
4. see the response similar to `Thank you for your request=abc on 2023-03-07T15:21:51.508Z`

# About this Demo
## contract-sdk
The [contract-sdk](./contract-sdk) project defines protobuf files, and will build and publish a maven jar to google's artifact registry.

- The client uses this contract to build sending/receiving logic around strong types and well defined interfaces
- The server implements a given version of the contract, so as long as the client and server are using the same SDK version we know they will be able to communicate
