package calendar

import org.specs2.mutable.Specification
import calendar.google.GoogleCalendar

class CalendarSpec extends Specification {

  "Calendar" should {

    "retrieve the next incoming events" in {
      val events = GoogleCalendar.nextIncomingEvents()

      events must not beNull
    }

  }

}