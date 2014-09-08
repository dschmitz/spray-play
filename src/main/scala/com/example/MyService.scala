package com.example

import akka.actor.Actor
import play.api.libs.json._
import spray.routing._
import spray.routing.Directive.pimpApply

import spray.http._
import MediaTypes._
import spray.httpx._
import spray.httpx.PlayJsonSupport
import spray.httpx.encoding._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with PlayJsonSupport {
  var count: Int = 0
  case class Person(name: String, surname: String)
  object Person {
    implicit val personFmt = Json.format[Person]
  }

  var personStore = Map.empty[Int, Person]

  val myRoute =
    path("person" / IntNumber) { personId =>
      get {
        val result = personStore.get(personId)
        if (result.isEmpty) {
          respondWithStatus(StatusCodes.NotFound) {
            complete { "no person was found for id: " + personId }
          }
        } else {
          respondWithMediaType(`application/json`) {
            respondWithStatus(StatusCodes.OK) {
              complete {
                result
              }
            }
          }
        }
      }
    } ~
      path("person") {
        post {
          entity(as[Person]) { person =>
            // starting by id 1
            count += 1
            personStore += count -> person
            respondWithMediaType(`application/json`) {
              respondWithStatus(StatusCodes.Created) {
                respondWithHeader(HttpHeaders.Location(s"person/$count")) {
                  complete {
                    person
                  }
                }
              }
            }
          }
        }
      }
}