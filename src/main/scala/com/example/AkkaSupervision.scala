package com.example

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props}

object PrinterProtocol {
  case class Message(msg: String)
}

class RestartMeException extends Exception("RESTART")
class ResumeMeException extends Exception("RESUME")
class StopMeException extends Exception("STOP")

class PrinterActor extends Actor {

  import PrinterProtocol._

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Yo, I am restarting...")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("...restart completed!")
    super.postRestart(reason)
  }

  override def preStart(): Unit = println("Yo, I am alive!")
  override def postStop(): Unit = println("Goodbye world!")

  override def receive: Receive = {
    case Message(msg) if containsRestart(msg) =>
      println(msg); throw new RestartMeException
    case Message(msg) if containsResume(msg) =>
      println(msg); throw new ResumeMeException
    case Message(msg) if containsStop(msg) =>
      println(msg); throw new StopMeException
    case Message(msg) if containsSecret(msg) =>
      println(msg); throw new Throwable
    case Message(msg) => println(msg)
  }

  private def containsRestart = containsWordCaseInsensitive("restart")_
  private def containsResume = containsWordCaseInsensitive("resume")_
  private def containsStop = containsWordCaseInsensitive("stop")_
  private def containsSecret = containsWordCaseInsensitive("secret")_

  private def containsWordCaseInsensitive(word: String)(msg: String) =  msg matches s".*(?i)$word.*"
}

class PrinterActorSupervisor extends Actor {

  override def preStart() = println("The Supervisor is ready to supervise")
  override def postStop() = println("Bye Bye from the Supervisor")

  override def supervisorStrategy = OneForOneStrategy() {
    case _: RestartMeException => Restart
    case _: ResumeMeException => Resume
    case _: StopMeException => Stop
  }

  val printer = context.actorOf(Props(new PrinterActor), "printer-actor")

  override def receive: Receive = {
    case msg => printer forward msg
  }
}

object AkkaSupervision extends App {
  import PrinterProtocol._

  implicit val system = ActorSystem("printer-service")
  val printerSupervisor = system.actorOf(Props(new PrinterActorSupervisor), "printer-supervisor")

  printerSupervisor ! Message("...please, print me...")

  printerSupervisor ! Message("...another message to print, nothing should happen...")

  printerSupervisor ! Message("...why don't you restart?!")

  printerSupervisor ! Message("...fell free to resume!")

  printerSupervisor ! Message("...you can STOP now!")

  printerSupervisor ! Message("...this is going to be our little secret...")

}
