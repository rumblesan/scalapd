package com.rumblesan.scalapd

import com.rumblesan.scalapd.network.{PDComs, PDConnection, PDMessage, PDChannel}

import akka.actor._


/** This is the actor that starts the PD process as well as handling message passing
  * between it and the rest of the app
  */

class PureDataManager() extends Actor {

  val listenerProps = Props(new PureDataListener)
  val pdProcess:ActorRef = context.actorOf(Props(new PureDataProcess(listenerProps)))

  var pdComs: Option[PDComs] = None

  var running: Boolean = false

  var channel: Option[PDChannel] = None

  def receive = {

    case StartPD(exe, port, patch, paths, extraArgs) => {

      pdComs = Some(new PDComs(port, self))

      if (running) {
        println("Already Running")
      } else {
        pdProcess ! StartPD(exe, port, patch, paths, extraArgs)

        running = true
      }
    }

    case KillPd() => {
      channel.map(_.channel.close())
      pdComs.map(_.serverChannel.close())
      pdProcess ! KillPd()
    }

    case PDConnection(connection) => {
      channel = Some(connection)
    }

    case SendPDMessage(message) => {
      channel.map(_.write(message))
    }

    case PDMessage(message) => {
      println("Message from PD:  %s".format(message))
    }

  }

}

case class SendPDMessage(message: List[String])

