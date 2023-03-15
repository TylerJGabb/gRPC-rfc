import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.{BroadcastHub, Keep, MergeHub, Sink, Source}
import contract.sdk.proto.{PiiRequest, PiiResponse, PiiService}

import scala.concurrent.Future

class PiiServiceImpl(system: ActorSystem[_]) extends PiiService {
  private implicit val sys: ActorSystem[_] = system

  val (inboundHub: Sink[PiiRequest, NotUsed], outboundHub: Source[PiiResponse, NotUsed]) =
    MergeHub.source[PiiRequest]
      .map(request => PiiResponse(s"STREAM mocked query result query=${request.query}, token=${request.token}"))
      .toMat(BroadcastHub.sink[PiiResponse])(Keep.both)
      .run()

  override def executePiiQuery(in: PiiRequest): Future[PiiResponse] = {
    Future.successful(PiiResponse(s"SINGULAR: mocked query result query=${in.query}, token=${in.token}"))
  }

  override def streamPiiQuery(in: Source[PiiRequest, NotUsed]): Source[PiiResponse, NotUsed] = {
    in.runWith(inboundHub)
    outboundHub
  }
}
