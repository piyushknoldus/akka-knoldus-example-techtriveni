package com.example

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import org.slf4j.LoggerFactory

case class Increment(num : Int)

case object GetCount

class IncrementCount extends Actor {
  val logger = LoggerFactory.getLogger(getClass.getSimpleName)
  var count: Int = 0

  override def receive: Receive = {
    case Increment(x) => {
      count += x
      logger.info(sender().path + " incrementing the counter " + count)
    }
  }
}

object CountExample extends App {
  val system = ActorSystem("Actor-System1")
  val incrementActor = system.actorOf(Props[IncrementCount],"incrementActor")

  for(i <- 1 to 10) {
    new Thread {
      override def run(): Unit = {
        for(j <- 1 to 10) {
          incrementActor.tell(Increment(1),incrementActor)
        }
      }
    }.start()
  }

  Thread.sleep(5000)
  system.terminate()
}