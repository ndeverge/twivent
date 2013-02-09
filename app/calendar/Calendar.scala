package calendar

import org.joda.time.DateTime

trait Calendar {

  def nextIncomingEvents(): Seq[Event]
}

case class Event(title: String, location: Option[String] = None, description: Option[String] = None, start: DateTime, end: Option[DateTime] = None)