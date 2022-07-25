
import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}

import java.net.InetSocketAddress

object RelayServer {
  def props(listenPort: Int) = Props(new RelayServer(listenPort))
}

class RelayServer(listenPort: Int) extends Actor with ActorLogging {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", listenPort))


  def receive: Receive = {
    case Bound(localAddress) =>
      log.info(s"Relay Server bounded successfully on listenPort: ${localAddress.getPort}")

    case CommandFailed(_: Bind) =>
      context.stop(self)

    case Connected(remote, local) =>
      val connection = sender()
      log.info(s"New relayed client from ${remote.getHostName}:${remote.getPort}")
      val handler = context.actorOf(RelayClient.props(connection), s"main.scala.RelayClient-${remote.getPort}")
      connection ! Register(handler)

  }


}
