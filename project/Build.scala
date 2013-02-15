import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "twivent"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
		  "com.google.apis" % "google-api-services-calendar" % "v3-rev24-1.13.2-beta", // exclude com.google.guava#guava-jdk5;13.0!guava-jdk5.jar ?
		  "com.google.http-client" % "google-http-client-jackson2" % "1.13.1-beta",
		  "org.twitter4j" % "twitter4j-core" % "3.0.3"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "google-api-services" at "http://google-api-client-libraries.appspot.com/mavenrepo"
          
  )

}
