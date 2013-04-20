package com.rumblesan.scalapd

import org.specs2.mutable._
import akka.actor._

import akka.testkit.TestActorRef
import akka.actor.ActorSystem

class PureDataSpec extends Specification {

  "The PureData class" should {

    "be instantiated correctly" in {
      implicit val system = ActorSystem("testsystem")
      val listenerProps = Props(new PureDataListener)
      val pd = TestActorRef(new PureDataProcess(listenerProps))
      pd.underlyingActor must haveClass[PureDataProcess]
      pd ! PoisonPill
    }

    "be capable of running up a PD process" in {
      val pdPath = "/Applications/Pd-extended.app/Contents/Resources/bin/pdextended"
      val patch  = "/Users/guy/repositories/patchwerk/patches/test.pd"

      implicit val system = ActorSystem("testsystem")
      val listenerProps = Props(new PureDataListener)
      val pd = TestActorRef(new PureDataProcess(listenerProps))

      pd ! StartPD(pdPath, 12345, patch, List.empty[String], List.empty[String])

      Thread.sleep(4000)

      pd.underlyingActor must haveClass[PureDataProcess]

      pd ! KillPd()

      pd ! PoisonPill

    }

  }

}

