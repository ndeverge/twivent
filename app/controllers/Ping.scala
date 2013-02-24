package controllers

import akka.pattern.ask
import play.api._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

object Ping extends Controller {

  def ping = Action {
    Ok("Alive !!")
  }

}