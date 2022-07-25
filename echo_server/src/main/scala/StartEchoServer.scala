import akka.actor.ActorSystem

import java.net.InetSocketAddress

object StartEchoServer extends App {

  val actorSystem = ActorSystem()
  val (host, port) = args.toList match {
    case host :: port :: Nil =>
      (host, port.toInt)
    case _ => ("localhost", 8080)
  }

  println("Starting Echo Server")

  val relayServerAddress = new InetSocketAddress("localhost", port)
  actorSystem.actorOf(EchoServer.props(relayServerAddress), "EchoServer")

}
