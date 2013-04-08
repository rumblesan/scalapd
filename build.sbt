name := "ScalaPd"

organization := "com.rumblesan"

version := "0.1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.14" % "test"
)

initialCommands := "import com.rumblesan.scalapd._"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "jboss repo" at "http://repository.jboss.org/nexus/content/groups/public-jboss/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.2"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.1.2"

libraryDependencies += "org.jboss.netty" % "netty" % "3.2.7.Final"

