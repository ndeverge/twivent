import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "twivent"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
		  "com.google.apis" % "google-api-services-calendar" % "v3-rev24-1.13.2-beta" // exclude com.google.guava#guava-jdk5;13.0!guava-jdk5.jar ?
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "google-api-services" at "http://google-api-client-libraries.appspot.com/mavenrepo"
          
  )

}
