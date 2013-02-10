package calendar

import org.specs2.mutable.Specification
import calendar.google.GoogleCalendar
import play.api.test.FakeApplication.apply
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import play.api.Play
import com.google.api.services.calendar.model.Event
import java.util.TimeZone

class GoogleCalendarSpec extends Specification {

  "GoogleCalendarAPI" should {

    "allow the app to authenticate" in {

      running(FakeApplication()) {

        GoogleCalendar.calendarService must beSome

      }
    }

    "retrieve my calendar" in {

      running(FakeApplication()) {

        GoogleCalendar.calendar.map(c => c.getSummary()) must beEqualTo(Some("We Meet In Toulouse"))

      }
    }

    "retrieve the incoming events" in {
      running(FakeApplication()) {

        val events = GoogleCalendar.nextIncomingEvents

        events must beSome

        events.get.size must beGreaterThan(0)

      }
    }

  }
}