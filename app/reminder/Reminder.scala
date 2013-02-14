package reminder

import calendar.Event
import org.joda.time.DateTime

object Reminder {

  def filterEventsToNotify(events: List[Event]) = {
    val dateIn24Hours = new DateTime().plusDays(1)
    events.filter(event => dateWithinBounds(event.start) && !alreadyNotified(event))
  }

  private def dateWithinBounds(date: DateTime): Boolean = {
    val dateIn24Hours = new DateTime().plusDays(1)
    val oneDayInMillis = 24 * 60 * 60 * 1000
    math.abs(date.getMillis() - dateIn24Hours.getMillis()) < oneDayInMillis
  }

  private def alreadyNotified(event: Event): Boolean = {
    event.notified
  }

}