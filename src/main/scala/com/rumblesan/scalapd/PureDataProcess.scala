package com.rumblesan.scalapd

import akka.actor._

import scala.sys.process._

import util._


object PureData {

  def createPdArgs(exe:String,
                   port:Int,
                   patch:String,
                   paths:List[String],
                   extraArgs:List[String]): List[String] = {

      val basicArgs = List(exe,
                           "-stderr",
                           "-nogui",
                           "-open",
                           patch,
                           "-send",
                           "startup port %d".format(port))

      val fullPaths = paths.foldLeft(List.empty[String])(
        (total, current) => {
          "-path" :: current :: total
        }
      )

      basicArgs ::: fullPaths ::: extraArgs
  }

}

class PureDataProcess(loggingActor: Props) extends Actor {

  val listener = context.actorOf(loggingActor)

  val subprocess = context.actorOf(Props[SubProcess])

  def receive = {

    case StartPD(exe, port, patch, paths, extraArgs) => {

      val args = PureData.createPdArgs(exe, port, patch, paths, extraArgs)

      val l = ProcessLogger(
        line => listener ! LogMessage(line),
        line => listener ! LogMessage(line)
      )

      val p = Process(args)

      subprocess ! SubProcessRun(p, l)

    }

  }

}

case class StartPD(exe:String, port:Int, patch:String, paths:List[String], extraArgs:List[String])


