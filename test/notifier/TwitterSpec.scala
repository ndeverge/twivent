package notifier

import org.specs2.mutable.Specification
import twitter4j.conf.ConfigurationBuilder
import twitter4j.TwitterFactory
import twitter4j.Status
import play.api.Play
import play.api.test.FakeApplication.apply
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import calendar.Event
import org.joda.time.DateTime

class TwitterSpec extends Specification {

  "Twitter API" should {
    "be able to authenticate to Twitter API" in {

      running(FakeApplication()) {
        Twitter.twitter must beSome

        Twitter.twitter.get.getScreenName() must not beNull
      }
    }

    "build a tweet from an Event without Url less than 140 characters" in {

      val eventToTweet = Event(id = "id1", title = "Event to tweet", start = new DateTime(), url = "")

      val tweet = Twitter.buildTweetFromEvent(eventToTweet)

      tweet.size must beLessThanOrEqualTo(140)
      tweet must contain(eventToTweet.title)
    }

    "build a tweet from an Event with a HTTP Url less than 140 characters" in {

    }

    "build a tweet from an Event with a HTTPS Url less than 140 characters" in {

    }
  }

}