package actor

import akka.actor.Actor
import play.api.Logger
import calendar.google.GoogleCalendar
import reminder.Reminder
import notifier.Twitter

class ReminderActor extends Actor {

  def receive = {
    case _ => {
      Logger.debug("Looking for events to notify...")
      val nextIncomingEvents = GoogleCalendar.nextIncomingEvents()
      Reminder.filterEventsToNotify(nextIncomingEvents).foreach {
        eventToNotify =>
          {
            GoogleCalendar.markAsNotified(eventToNotify.id)

            Twitter.sendTweet(eventToNotify).map(tweet => Logger.info("Tweeting " + tweet.getText()))

          }
      }
    }
  }
}