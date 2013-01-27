import scala.concurrent.duration.DurationInt
import akka.actor.Props
import play.api.Application
import play.api.GlobalSettings
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.libs.Akka
import play.api.Play

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    // get the port from the "http.port" environment variable
    val runningPort = Play.configuration(app).getInt("http.port").orElse(Some(9000)).get
    val urlToPing = "http://localhost:%d%s".format(runningPort, controllers.routes.Ping.ping.url)

    val pingActor = Akka.system.actorOf(Props(new PingActor(urlToPing)))
    Akka.system.scheduler.schedule(0 seconds, 10 minutes, pingActor, "ping")
  }

}