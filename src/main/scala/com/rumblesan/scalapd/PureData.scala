package com.rumblesan.scalapd

import akka.actor._


case class SendPDMessage(message: List[String])


trait FileLogger {

  import java.io.{BufferedWriter, FileWriter}

  val logFileName: String
  val fileOut = new BufferedWriter(new FileWriter(logFileName))

  def writeToLog(output: String) {
    fileOut.write(output + "\n")
  }

}

trait PrintLogger {

  def writeToLog(output: String) {
    println(output)
  }

}


trait PureDataProcess {

  import scala.sys.process._

  def createPdArgList(exe:String,
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

  def startPdProcess(args: List[String]) = {
      val logger = ProcessLogger(
        line => pdProcessStdOut(line),
        line => pdProcessStdErr(line)
      )

      val p = Process(args)
      val result = p ! logger
      pdProcessFinished(result)
  }

  def pdProcessStdOut(line: String): Unit

  def pdProcessStdErr(line: String): Unit

  def pdProcessFinished(result: Int): Unit

}


case class StartPD(exe:String, port:Int, patch:String, paths:List[String], extraArgs:List[String])

class PureData() extends Actor with PureDataProcess with PrintLogger {

  def pdProcessStdOut(line: String) {
    writeToLog(line)
  }

  def pdProcessStdErr(line: String) {
    writeToLog(line)
  }

  def pdProcessFinished(result: Int) {
    writeToLog("PD finished with status %d".format(result))
  }

  def receive = {

    case StartPD(exe, port, patch, paths, extraArgs) => {
      val args = createPdArgList(exe, port, patch, paths, extraArgs)
      startPdProcess(args)
    }

  }

}

