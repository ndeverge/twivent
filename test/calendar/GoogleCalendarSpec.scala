package calendar

import org.specs2.mutable.Specification
import calendar.google.GoogleCalendar
import play.api.test.FakeApplication.apply
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import play.api.Play

class GoogleCalendarSpec extends Specification {

  "GoogleCalendarAPI" should {

    "allow the app to authenticate" in {

      running(FakeApplication()) {

        println(GoogleCalendar.calendar.getSummary())
      }
    }

  }
}