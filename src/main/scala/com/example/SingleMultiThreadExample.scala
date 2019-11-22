package com.example

import org.slf4j.LoggerFactory

class HelloWorld {
  val logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def sayHelloAndDoMore(name :String): Unit = {
    logger.info("Hello " + name)
    doSomething
  }

  def doSomething(): Unit = {
    Thread.sleep(3000)
  }
}

object SingleMultiThreadExample extends App {

  val logger = LoggerFactory.getLogger(getClass.getSimpleName)

  val allUsers = List("user1", "user2", "user3", "user4", "user5")

  val helloWorld = new HelloWorld
  sayHelloToAllSingleThreaded(allUsers)
  sayHelloToAllMultipleThread(allUsers)


  def sayHelloToAllSingleThreaded(users: List[String]): Unit = {
    logger.info("Saying Hello by single threaded")
    users.foreach {
      user => helloWorld.sayHelloAndDoMore(user)
    }
  }

  def sayHelloToAllMultipleThread(users: List[String]): Unit = {
    logger.info("Saying Hello by using multiple thread")
    users.foreach(
      user => {
        val thread: Thread = new Thread {
          override def run(): Unit = {
            helloWorld.sayHelloAndDoMore(user)
          }
        }
        thread.start()
      })
  }
}
