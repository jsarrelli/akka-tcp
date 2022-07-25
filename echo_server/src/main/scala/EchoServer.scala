import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}

import java.net.InetSocketAddress

class EchoServer(remote: InetSocketAddress) extends Actor with ActorLogging {

  import Tcp._
  import context.system

  println("Connecting to Relay Server")
  IO(Tcp) ! Connect(remote)

  def receive: Receive = {
    case CommandFailed(_: Connect) =>
      context.stop(self)

    case Connected(remote, local) =>
      log.info("Connected to Relay Server")
      val connection = sender()
      val handler = context.actorOf(Props[EchoHandler](), "EchoServerHandler")
      connection ! Register(handler)
  }
}

object EchoServer {
  def props(remote: InetSocketAddress): Props =
    Props(new EchoServer(remote))
}