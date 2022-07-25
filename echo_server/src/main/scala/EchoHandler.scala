import akka.actor.{Actor, ActorLogging}
import akka.io.Tcp._

class EchoHandler extends Actor with ActorLogging {
  def receive: Receive = {
    case Received(data) =>
      log.info(s"Incoming ${data.utf8String}")
      sender() ! Write(data)
    case _: ConnectionClosed =>
      log.info("Connection closed")
      context.stop(self)
    case PeerClosed => context.stop(self)
  }
}