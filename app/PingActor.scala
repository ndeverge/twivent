import akka.actor.Actor
import play.api.Logger
import play.api.libs.ws.WS
import java.text.SimpleDateFormat
import java.util.Date
import play.api.libs.concurrent.Execution.Implicits._

class PingActor(url: String) extends Actor {

  def receive = {
    case _ => {
      WS.url(url).get.map(response => {
        val now = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())
        Logger.info(now + ": ping status: " + response.status)
      })
    }
  }

}