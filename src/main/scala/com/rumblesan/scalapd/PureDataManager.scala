package com.rumblesan.scalapd

import com.rumblesan.scalapd.network.{PDComs, PDConnection, PDMessage, PDChannel}

import akka.actor._


/** This is the actor that starts the PD process as well as handling message passing
  * between it and the rest of the app
  */

class PureDataManager(port: Int) extends Actor {

  val listenerProps = Props(new PureDataListener)
  val pdProcess:ActorRef = context.actorOf(Props(new PureDataProcess(listenerProps)))
  val pdComs = new PDComs(port, self)

  var running: Boolean = false

  var channel: PDChannel = null

  def receive = {

    case StartPD(exe, port, patch, paths, extraArgs) => {
      if (running) {
        println("Already Running")
      } else {
        pdProcess ! StartPD(exe, port, patch, paths, extraArgs)

        running = true
      }
    }

    case PDConnection(connection) => {
      channel = connection
    }

    case SendPDMessage(message) => {
      channel.write(message)
    }

    case PDMessage(message) => {
      println("Message from PD:  %s".format(message))
    }

  }

}

case class SendPDMessage(message: List[String])

