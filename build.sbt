name := """HRMS"""

version := "1.0.0"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "com.h2database" % "h2" % "1.4.191",
  "com.github.tminglei" %% "slick-pg_joda-time" % "0.14.3",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "com.fsist" %% "safepickle-joda-time" % "1.0.9",
  "com.roundeights" %% "hasher" % "1.2.0",
  "org.postgresql" % "postgresql" % "9.4-1204-jdbc42"
)


unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
