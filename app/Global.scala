import scala.Some.apply
import scala.annotation.implicitNotFound
import scala.concurrent.duration.DurationInt
import akka.actor.Props.apply
import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.libs.Akka
import akka.actor.Props

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    // get the port from the "http.port" environment variable
    //val runningPort = Play.configuration(app).getInt("http.port").orElse(Some(9000)).get
    val runningPort = System.getProperty("http.port").orElse("9000")
    val urlToPing = "http://localhost:%d%s".format(runningPort, controllers.routes.Ping.ping.url)

    val pingActor = Akka.system.actorOf(Props(new PingActor(urlToPing)))
    Logger.info("Scheduling ping on " + urlToPing)
    Akka.system.scheduler.schedule(0 seconds, 10 minutes, pingActor, "ping")
  }

}