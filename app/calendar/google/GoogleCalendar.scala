package calendar.google

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.calendar.CalendarScopes
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.KeyFactory
import play.api.Play
import com.google.api.client.util.Base64
import collection.JavaConversions._
import collection.JavaConverters._
import com.google.api.client.util.DateTime
import java.util.Date
import java.util.TimeZone
import calendar.Event
import play.api.Logger
import com.google.api.services.calendar.model.Event.ExtendedProperties
import java.util.HashMap

object GoogleCalendar extends config.Config {

  lazy val calendarService: Option[com.google.api.services.calendar.Calendar] = {

    for (
      accountId <- getPropertyFromConfOrEnvironment("google-calendar.accountId");
      privateKey <- getPropertyFromConfOrEnvironment("google-calendar.privateKey");
      applicationName <- getPropertyFromConfOrEnvironment("google-calendar.applicationName")
    ) yield {

      val HTTP_TRANSPORT = new NetHttpTransport()
      val JSON_FACTORY = new JacksonFactory()

      val encoded = Base64.decodeBase64(privateKey.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", ""));
      val keyFactory = KeyFactory.getInstance("RSA");
      val ks = new PKCS8EncodedKeySpec(encoded);
      val key = keyFactory.generatePrivate(ks);

      val credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
        .setJsonFactory(JSON_FACTORY)
        .setServiceAccountId(accountId)
        .setServiceAccountScopes(CalendarScopes.CALENDAR)
        .setServiceAccountPrivateKey(key)
        .build();

      new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
        applicationName).build()

    }

  }

  lazy val calendar = {
    getPropertyFromConfOrEnvironment("google-calendar.calendarId").flatMap {
      calendarId =>
        calendarService.map {

          service => service.calendars().get(calendarId).execute()
        }
    }
  }

  def nextIncomingEvents(): List[Event] = {

    getPropertyFromConfOrEnvironment("google-calendar.calendarId").flatMap {
      calendarId =>
        calendarService.map {

          val now = new DateTime(new Date(), TimeZone.getTimeZone("Europe/Paris"))
          service => service.events().list(calendarId).setSingleEvents(true).setTimeMin(now).setOrderBy("startTime").execute().getItems().toList
        }
    } match {
      case None => List()
      case Some(eventList) => eventList.map(toEvent(_))
    }

  }

  def findEventById(eventId: String) = {
    Play.current.configuration.getString("google-calendar.calendarId").flatMap {
      calendarId =>
        val now = new DateTime(new Date(), TimeZone.getTimeZone("Europe/Paris"))
        GoogleCalendar.calendarService.map(service => service.events().get(calendarId, eventId).execute())

    }
  }

  val NOTIFICATION_24HOURS = "notified24HoursBefore"

  def markAsNotified(eventId: String) = {
    findEventById(eventId) map {
      eventToMark =>

        Logger.info("\"%s\" at %s has been marked as notified".format(eventToMark.getSummary(), eventToMark.getStart()))

        Play.current.configuration.getString("google-calendar.calendarId").flatMap {
          calendarId =>
            GoogleCalendar.calendarService.map(service => service.events().update(calendarId, eventId, addCustomProperty(eventToMark)).execute())

        }
    }

  }

  def wasNotified(event: com.google.api.services.calendar.model.Event): Boolean = {
    Option(event.getExtendedProperties()) match {
      case None => false
      case Some(properties) => Option(properties.getShared()) match {
        case None => false
        case Some(shared) => shared.get(NOTIFICATION_24HOURS) != null
      }
    }
  }

  private def addCustomProperty(event: com.google.api.services.calendar.model.Event) = {
    val properties: java.util.Map[String, String] = Option(event.getExtendedProperties()) match {
      case None => new HashMap[String, String]()
      case Some(extendedProperties) => extendedProperties.getShared()
    }

    properties.put(NOTIFICATION_24HOURS, String.valueOf(System.currentTimeMillis()));

    event.setExtendedProperties(new ExtendedProperties().setShared(properties))

  }

  private def toEvent(googleEvent: com.google.api.services.calendar.model.Event) = {
    Event(googleEvent.getId(), googleEvent.getSummary(), Option(googleEvent.getLocation()), Option(googleEvent.getDescription()), googleEvent.getStart(), Option(googleEvent.getEnd()), googleEvent.getHtmlLink(), wasNotified(googleEvent))
  }

  /**
   * Implicit conversion (no explanatary call needed)
   */
  implicit def toDateTime(googleDateTime: com.google.api.services.calendar.model.EventDateTime): org.joda.time.DateTime = {
    if (googleDateTime != null && googleDateTime.getDateTime() != null) {
      new org.joda.time.DateTime(googleDateTime.getDateTime().getValue())
    } else {
      new org.joda.time.DateTime(googleDateTime.getDate().getValue()).withTimeAtStartOfDay()
    }
  }
}