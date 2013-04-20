package com.rumblesan.scalapd

import org.specs2.mutable._
import akka.actor._

import akka.testkit.TestActorRef
import akka.actor.ActorSystem

class PureDataManagerSpec extends Specification {

  "The PureDataManager class" should {

    "be instantiated correctly" in {
      implicit val system = ActorSystem("testsystem")
      val manager = TestActorRef(new PureDataManager())
      manager.underlyingActor must haveClass[PureDataManager]

      Thread.sleep(12000)

      manager ! KillPd()
      manager ! PoisonPill
    }

    "run up a PD process" in {
      implicit val system = ActorSystem("testsystem")
      val manager = TestActorRef(new PureDataManager())

      val pdPath = "/Applications/Pd-extended.app/Contents/Resources/bin/pdextended"
      val patch  = "/Users/guy/repositories/patchwerk/patches/test.pd"

      val start = StartPD(pdPath, 12345, patch, List.empty[String], List.empty[String])

      manager ! start

      Thread.sleep(12000)

      manager.underlyingActor must haveClass[PureDataManager]
      manager ! KillPd()
      manager ! PoisonPill
    }

  }

}

