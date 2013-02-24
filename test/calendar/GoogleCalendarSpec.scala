package calendar

import org.specs2.mutable.Specification
import calendar.google.GoogleCalendar
import play.api.test.FakeApplication.apply
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import play.api.Play
import java.util.TimeZone
import com.google.api.client.util.DateTime
import java.util.Date
import collection.JavaConversions._

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

        events must not beNull

        events.size must beGreaterThan(0)

      }
    }

    "mark an event as notified" in {
      running(FakeApplication()) {

        val anOldEvent = Play.current.configuration.getString("google-calendar.calendarId").flatMap {
          calendarId =>
            val now = new DateTime(new Date(), TimeZone.getTimeZone("Europe/Paris"))
            GoogleCalendar.calendarService.map(service => service.events().list(calendarId).setTimeMax(now).execute().getItems().toList)

        } map {
          events => events.head
        }

        anOldEvent.map(event => GoogleCalendar.wasNotified(event) must beFalse)

        GoogleCalendar.markAsNotified(anOldEvent.map(oldEvent => oldEvent.getId()).get) map {
          updatedEvent => GoogleCalendar.wasNotified(updatedEvent.get) must beTrue
        }

        val reloadedEvent = GoogleCalendar.findEventById(anOldEvent.get.getId())

        reloadedEvent must beSome
        reloadedEvent.get.getId() must beEqualTo(anOldEvent.get.getId())

        reloadedEvent.foreach(GoogleCalendar.wasNotified(_) must beTrue)

      }
    }

  }
}