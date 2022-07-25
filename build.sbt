name := "bluematador"

version := "0.1"

scalaVersion := "2.13.8"
val AkkaVersion = "2.6.19"

lazy val commonDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion
)

lazy val global = project
  .in(file("."))
  .aggregate(
    relay_server,
    echo_server
  )

lazy val relay_server = project.settings(
  libraryDependencies ++= commonDependencies
)
lazy val echo_server = project.settings(
  libraryDependencies ++= commonDependencies
)


lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar"
)