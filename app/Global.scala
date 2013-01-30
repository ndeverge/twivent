import scala.Some.apply
import scala.annotation.implicitNotFound
import scala.concurrent.duration.DurationInt
import akka.actor.Props.apply
import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Akka
import akka.actor.Props
import actor.PingActor

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    val controllerPath = controllers.routes.Ping.ping.url
    play.api.Play.mode(app) match {
      case play.api.Mode.Prod => schedulePing("http://twivent.herokuapp.com%s".format(controllerPath), app)
      case _ => // do not schedule anything for Dev or Test
    }

  }

  def schedulePing(urlToPing: String, app: Application) = {
    Logger.info("Scheduling ping on " + urlToPing)
    val pingActor = Akka.system(app).actorOf(Props(new PingActor(urlToPing)))
    Akka.system(app).scheduler.schedule(0 seconds, 10 minutes, pingActor, "ping")
  }

}