package reminder

import calendar.Event
import org.joda.time.DateTime

object Reminder {

  def filterEventsToNotify(events: List[Event]) = {
    events.filter(event => dateWithinBounds(event.start) && !alreadyNotified(event))
  }

  private def dateWithinBounds(date: DateTime): Boolean = {
    val now = System.currentTimeMillis()
    val oneDayInMillis = 24 * 60 * 60 * 1000

    date.getMillis() - now > 0 && date.getMillis() - now < oneDayInMillis
  }

  private def alreadyNotified(event: Event): Boolean = {
    event.notified
  }

}