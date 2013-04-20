import sbt._
import sbt.Keys._

object ScalaPdBuild extends Build {

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(

    organization := "com.rumblesan",

    version := "0.1.0",

    scalaVersion := "2.10.1",
    crossScalaVersions := Seq("2.9.1", "2.9.2", "2.10.1"),

    resolvers += "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    resolvers += "sonatype releases" at "http://oss.sonatype.org/content/repositories/releases",
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

  )

  // This helps make sure we get the correct specs2 version
  // there are different versions for scala 2.9 and 2.10
  def specs2Dependencies(scalaVersion: String) = {
    val Old = """2\.9\..*""".r
    scalaVersion match {
      case Old() => Seq("org.specs2" %% "specs2" % "1.12.3" % "test")
      case _ => Seq("org.specs2" %% "specs2" % "1.14" % "test")
    }
  }

  def akkaDependencies(scalaVersion: String) = {
    val Old = """2\.9\..*""".r
    scalaVersion match {
      case Old() => Seq(
        "com.typesafe.akka" % "akka-actor" % "2.0.5",
        "com.typesafe.akka" % "akka-testkit" % "2.0.5" % "test"
      )
      case _ => Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.1.2",
        "com.typesafe.akka" %% "akka-testkit" % "2.1.2" % "test"
      )
    }
  }


  // Dependencies.
  lazy val mockito = "org.mockito" % "mockito-core" % "1.8.5" % "test"
  lazy val netty = "org.jboss.netty" % "netty" % "3.2.7.Final"

  lazy val defaultSettings = Defaults.defaultSettings ++ buildSettings ++ Seq(
    libraryDependencies <++= scalaVersion(specs2Dependencies(_)),
    libraryDependencies <++= scalaVersion(akkaDependencies(_)),
    libraryDependencies += mockito,
    libraryDependencies += netty
  )

  lazy val scalapd = Project(
    id = "scalapd",
    base = file("."),

    settings = defaultSettings ++ buildSettings

  ).settings(
    resolvers += "jboss repo" at "http://repository.jboss.org/nexus/content/groups/public-jboss/"
  )


}
