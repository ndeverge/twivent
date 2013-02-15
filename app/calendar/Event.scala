package calendar

case class Event(
  id: String,
  title: String,
  location: Option[String] = None,
  description: Option[String] = None,
  start: org.joda.time.DateTime,
  end: Option[org.joda.time.DateTime] = None,
  url: String,
  notified: Boolean = false)
