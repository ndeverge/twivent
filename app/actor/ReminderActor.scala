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
            Logger.info("We have to notity '%s' with id '%s'".format(eventToNotify.title, eventToNotify.id))

            GoogleCalendar.markAsNotified(eventToNotify.id) match {
              case None => Logger.error("Unable to find the event with id '%s'".format(eventToNotify.id))
              case Some(_) => Twitter.sendTweet(eventToNotify).map(tweet => Logger.info("Tweeting " + tweet.getText()))
            }

          }
      }
    }
  }
}