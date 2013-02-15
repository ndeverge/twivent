package reminder

import org.specs2.mutable.Specification
import calendar.Event
import org.joda.time.DateTime

class ReminderSpec extends Specification {

  "Reminder" should {

    "filter events ready to be sent" in {

      val dateIn24Hours = new DateTime().plusDays(1)

      val anEventToNotify = Event(id = "id1", title = "Event to notify", start = dateIn24Hours, url = "")

      val dateFrom1Hour = new DateTime().minusHours(1)
      val anEventToNotNotify = Event(id = "id2", title = "Do not notify", start = dateFrom1Hour, url = "")

      val incomingEvents = List(anEventToNotNotify, anEventToNotify);

      val eventsToNotify = Reminder.filterEventsToNotify(incomingEvents)
      eventsToNotify must not beNull

      eventsToNotify must contain(anEventToNotify)
      eventsToNotify must not contain (anEventToNotNotify)

    }

    "do not filter events already notified" in {

      val anEventAlreadyNotified = Event(id = "id", title = "Do not notify", start = new DateTime().plusHours(1), notified = true, url = "")

      val incomingEvents = List(anEventAlreadyNotified);

      val eventsToNotify = Reminder.filterEventsToNotify(incomingEvents)
      eventsToNotify must not beNull

      eventsToNotify must beEmpty

    }
  }

}