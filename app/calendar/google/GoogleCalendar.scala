package calendar.google

import calendar.Calendar
import calendar.Event
import org.joda.time.DateTime
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.calendar.CalendarScopes
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.KeyFactory
import play.api.Play
import com.google.api.client.util.Base64

object GoogleCalendar extends Calendar {

  lazy val calendarService: com.google.api.services.calendar.Calendar = {

    val HTTP_TRANSPORT = new NetHttpTransport()
    val JSON_FACTORY = new JacksonFactory()

    val accountId = Play.current.configuration.getString("google-calendar.accountId").get // orElse from the env

    // openssl pkcs12 -in conf/keys/google-calendar.p12 -nodes -nocerts |  openssl pkcs8 -topk8 -nocrypt

    val privateKey: Option[String] = Play.current.configuration.getString("google-calendar.privateKey").map(s => s.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")) // orElse from the env

    // FIXME : if None

    val encoded = Base64.decodeBase64(privateKey.get);
    val keyFactory = KeyFactory.getInstance("RSA");
    val ks = new PKCS8EncodedKeySpec(encoded);
    val key = keyFactory.generatePrivate(ks);

    val credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
      .setJsonFactory(JSON_FACTORY)
      .setServiceAccountId(accountId)
      .setServiceAccountScopes(CalendarScopes.CALENDAR)
      .setServiceAccountPrivateKey(key)
      .build();

    val applicationName = Play.current.configuration.getString("google-calendar.applicationName").get

    new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
      applicationName).build()
  }

  override def nextIncomingEvents(): Seq[Event] = {
    Seq(new Event(title = "", start = new DateTime))
  }
}