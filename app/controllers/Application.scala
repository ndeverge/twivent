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
import calendar.google.GoogleCalendar

object Application extends Controller {

  def index = Action {

    val wmitCalendar = GoogleCalendar.calendar
    Ok("Index !!" + wmitCalendar.map(c => c.getSummary))
  }

}