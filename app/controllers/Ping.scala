package controllers

import play.api._
import play.api.mvc._

object Ping extends Controller {

  def ping = Action {
    Ok("Alive !!")
  }

}