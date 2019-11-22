package com.example

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import org.slf4j.LoggerFactory

class AkkaActorClass extends Actor {
  val logger = LoggerFactory.getLogger(getClass.getSimpleName)

  override def receive: Receive = {
    case "1" => logger.info("I am first case")
    case "2" => logger.info("I am second case")
    case _ => logger.info("handling all other cases")
  }
}

object Application extends App {
  val system = ActorSystem("Actor-System")
  val props = Props[AkkaActorClass]
  val actorRef: ActorRef = system.actorOf(props, "actor-1")
  actorRef ! "1"
  actorRef ! "2"
  actorRef ! "3"
  Thread.sleep(1000)
  system.terminate()
}


