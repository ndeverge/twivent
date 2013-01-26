import play.api.GlobalSettings
import play.api.Logger
import play.api.Application
import play.libs.Akka
import scala.concurrent.duration._
import akka.actor.Props
import akka.actor.Actor
import play.api.libs.concurrent.Execution.Implicits._
import java.text.SimpleDateFormat
import java.util.Date
import play.api.libs.ws.WS

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    val Tick = "tick"
    val tickActor = Akka.system.actorOf(Props(new Actor {
      def receive = {
        case Tick => {
          Logger.info("tock at " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
          WS.url("http://twivent.herokuapp.com").get.map(response => Logger.info("ping status: " + response.status))
        }
      }
    }))

    Akka.system.scheduler.schedule(0 seconds, 10 minutes, tickActor, "tick")
  }

}