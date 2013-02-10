package calendar.google

import org.joda.time.DateTime
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.calendar.CalendarScopes
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.KeyFactory
import play.api.Play
import com.google.api.client.util.Base64

case class Event(title: String, location: Option[String] = None, description: Option[String] = None, start: DateTime, end: Option[DateTime] = None)

object GoogleCalendar {

  lazy val calendarService: Option[com.google.api.services.calendar.Calendar] = {

    getPropertyFromConfOrEnvironment("google-calendar.accountId").flatMap {
      accountId =>
        getPropertyFromConfOrEnvironment("google-calendar.privateKey").flatMap {
          privateKey =>
            {
              getPropertyFromConfOrEnvironment("google-calendar.applicationName").map {
                applicationName =>

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
        }
    }

  }

  lazy val calendar = {
    getPropertyFromConfOrEnvironment("google-calendar.calendarId").map {
      calendarId => calendarService.get.calendars().get(calendarId).execute();
    }
  }

  def nextIncomingEvents(): Seq[Event] = {
    Seq(new Event(title = "", start = new DateTime))
  }

  private def getPropertyFromConfOrEnvironment(name: String): Option[String] = {
    Play.current.configuration.getString(name).orElse(Some(sys.env(name)))
  }
}