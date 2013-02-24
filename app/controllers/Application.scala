package controllers

import calendar.google.GoogleCalendar
import play.api.mvc.Action.apply
import play.api.mvc.Controller
import play.api.mvc.Action

object Application extends Controller {

  def index = Action {

    val wmitCalendar = GoogleCalendar.calendar
    Ok("Index !!" + wmitCalendar.map(c => c.getSummary))
  }

}