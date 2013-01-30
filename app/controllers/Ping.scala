package controllers

import play.api._
import play.api.mvc._
import akka.util.Timeout
import scala.concurrent.duration._
import play.libs.Akka
import akka.actor.Props
import actor.PingActor
import akka.pattern.ask
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Ping extends Controller {

  def ping = Action {
    Ok("Alive !!")
  }

  def index = Action {
    Async {
      implicit val timeout = Timeout(5.seconds)
      val pingActor = Akka.system.actorOf(Props(new PingActor("")))
      (pingActor ? "hello").mapTo[String].map { response =>
        Ok(response)
      }
    }
  }

}