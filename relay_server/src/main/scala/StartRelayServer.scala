
import akka.actor.ActorSystem

object StartRelayServer extends App {

  val actorSystem = ActorSystem()
  val port = args.headOption.map(_.toInt).getOrElse(8080)
  println("Starting Relay Server")
  actorSystem.actorOf(RelayServer.props(port), "RelayedServer")


}
