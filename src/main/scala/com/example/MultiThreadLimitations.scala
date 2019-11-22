package com.example

import org.slf4j.LoggerFactory

class MultithreadTest {
  val logger = LoggerFactory.getLogger(getClass.getSimpleName)

  var count = 0
  def incrementCount = {
    this.synchronized {
      logger.info(Thread.currentThread().getName + " Entering with counter value as" + count)
      try {
        count = count + 1
      }
      catch {
        case ex: Exception => ex.printStackTrace()
      }
      logger.info(Thread.currentThread().getName + " Leaving with counter value as" + count)
    }
  }
}

object MultiThreadLimitations extends App {
  val object1 = new MultithreadTest
  println("value of counter before incrementing " + object1.count)

  for(i <- 1 to 100) {
    val thread1 = new Thread {
      override def run(): Unit = {
        object1.incrementCount
      }
    }.start()
  }
}
