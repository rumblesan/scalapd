package com.rumblesan.scalapd

import org.specs2.mutable._
import akka.actor._

import akka.testkit.TestActorRef
import akka.actor.ActorSystem

class PureDataSpec extends Specification {

  "The PureData class" should {

    "be instantiated correctly" in {
      implicit val system = ActorSystem("testsystem")
      val manager = TestActorRef(new PureData)
      manager.underlyingActor must haveClass[PureData]
      manager ! PoisonPill
    }

  }

}

