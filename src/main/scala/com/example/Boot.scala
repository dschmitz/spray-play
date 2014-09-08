package com.example

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import com.example.MyWebsocket.WebSocketServer
import spray.can.server.UHttp

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[MyServiceActor], "demo-service")

  val server = system.actorOf(WebSocketServer.props(), "websocket")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 18080)


//  import system.dispatcher
//  IO(UHttp) ! Http.Bind(server, "localhost", 28080)
//
//  readLine("Hit ENTER to exit ...\n")
//  system.shutdown()
//  system.awaitTermination()
}
