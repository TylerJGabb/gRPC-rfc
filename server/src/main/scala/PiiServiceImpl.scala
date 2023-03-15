import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.{BroadcastHub, Keep, MergeHub, Sink, Source}
import contract.sdk.proto.{PiiRequest, PiiResponse, PiiService}

import scala.concurrent.Future

class PiiServiceImpl(system: ActorSystem[_]) extends PiiService {
  private implicit val sys: ActorSystem[_] = system

  val (inboundHub: Sink[PiiRequest, NotUsed], outboundHub: Source[PiiResponse, NotUsed]) =
    MergeHub.source[PiiRequest]
      .map { request =>
        val queryResult = s"STREAM mocked query result query=${request.query}, token=${request.token}"
        val bigqueryLabel = "BQ label from streaming"
        PiiResponse(
          queryResult = queryResult,
          bigqueryLabel = bigqueryLabel
        )
      }
      .toMat(BroadcastHub.sink[PiiResponse])(Keep.both)
      .run()

  override def executePiiQuery(in: PiiRequest): Future[PiiResponse] = {
    val queryResult = s"SINGULAR: mocked query result query=${in.query}, token=${in.token}"
    val bigqueryLabel = "BQ label from singular execution"
    val response = PiiResponse(
      queryResult,
      bigqueryLabel
    )
    Future.successful(response)
  }

  override def streamPiiQuery(in: Source[PiiRequest, NotUsed]): Source[PiiResponse, NotUsed] = {
    in.runWith(inboundHub)
    outboundHub
  }
}
