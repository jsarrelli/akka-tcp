import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.Tcp

object RelayClientHandler {
  def props(relayedClient: ActorRef, client: ActorRef): Props = Props(new RelayClientHandler(relayedClient, client))
}

class RelayClientHandler(relayedClient: ActorRef, client: ActorRef) extends Actor with ActorLogging {

  import Tcp._

  def receive: Receive = {
    case Received(data) if sender() == client =>
      relayedClient ! Write(data)
    case Received(data) if sender() == relayedClient =>
      client ! Write(data)
    case _: ConnectionClosed =>
      log.info("Connection closed")
      context.stop(self)
    case PeerClosed =>
      context.stop(self)
  }
}
