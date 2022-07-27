
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.{IO, Tcp}

import java.net.InetSocketAddress

object RelayClient {
  def props(relayedClient: ActorRef): Props = Props(new RelayClient(relayedClient))
}

class RelayClient(relayedClient: ActorRef) extends Actor with ActorLogging {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 0))
  var handler: ActorRef = ActorRef.noSender


  def receive: Receive = {
    case Bound(localAddress) =>
      log.info(s"Established relay address: ${localAddress.getAddress}:${localAddress.getPort}")

    case CommandFailed(_: Bind) =>
      context.stop(self)

    case Connected(remote, local) =>
      log.info(s"New client for relayed client:${local.getPort}")
      val connection = sender()
      handler = context.actorOf(ConnectionHandler.props(relayedClient, connection))
      connection ! Register(handler)

    case msg: Received =>
      //This is not actually okay if we want to have different clients under the same relayed client,
      // it cannot distinguish who is the actual sender and because of that, who to reply
      handler forward msg

  }

}
