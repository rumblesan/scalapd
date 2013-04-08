package com.rumblesan.scalapd.util

import akka.actor.Actor

import scala.sys.process.{ ProcessBuilder, ProcessLogger }


class SubProcess extends Actor {

  def receive = {

    case SubProcessRun(p, l) => {
      val result = p ! l
      sender ! SubProcessFinished(result)
    }

  }

}

case class SubProcessRun(p: ProcessBuilder, l: ProcessLogger)
case class SubProcessFinished(result: Int)

