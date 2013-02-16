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
import play.api.test.FakeApplication

class TwitterSpec extends Specification {

  "Twitter API" should {
    "be able to authenticate to Twitter API" in {

      running(FakeApplication()) {
        Twitter.twitter must beSome

        Twitter.twitter.get.getScreenName() must not beNull
      }
    }

    val shortURLLength = 23

    "build a tweet from an Event less than 140 characters" in {

      val eventToTweet = Event(id = "id1", title = "Event to tweet", start = new DateTime(), url = "")

      val tweet = Twitter.buildTweetFromEvent(eventToTweet, shortURLLength)

      tweet.size must beLessThanOrEqualTo(140)
      tweet must contain(eventToTweet.title)
    }

    "build a tweet from an Event with a very long title less than 140 characters" in {
      val veryVeryLongTitle = "This is a very very very very very very very very very very very very very very very very very very long title that is more that 140 characters"

      val eventToTweet = Event(id = "id1", title = veryVeryLongTitle, start = new DateTime(), url = "")

      val tweet = Twitter.buildTweetFromEvent(eventToTweet, shortURLLength)

      tweet.size must beLessThanOrEqualTo(140)
      tweet must contain(eventToTweet.title.substring(0, 50))
    }

    "build a tweet from an empty Event and report the size of the template" in {

      val eventToTweet = Event(id = "id1", title = "", start = new DateTime(), url = "")

      val tweet = Twitter.buildTweetFromEvent(eventToTweet, shortURLLength)

      tweet.size must beEqualTo(14)
    }

    "build a tweet from an Event with a HTTP Url and a fake Url shortener" in {

      val aLongTitle = "This event has a quite long title since it has to be used in order to test building a tweet containing an url"

      val url = "http://www.wemeetintoulouse.net"

      val eventToTweet = Event(id = "id1", title = aLongTitle, start = new DateTime(), url = url)

      val tweet = Twitter.buildTweetFromEvent(eventToTweet, shortURLLength)

      tweet.size must beEqualTo(140 - shortURLLength + url.size)
      tweet must contain(eventToTweet.url)
    }

    "build a tweet from an Event with a HTTP Url with the Url size got from Twitter API" in {
      running(FakeApplication()) {
        val shortURLLength = Twitter.shortURLLength

        val veryVeryLongTitle = "This is a very very very very very very very very very very very very very very very very very very long title that is more that 140 characters"

        val eventToTweet = Event(id = "id1", title = veryVeryLongTitle, start = new DateTime(), url = "")

        val tweet = Twitter.buildTweetFromEvent(eventToTweet)
        tweet.size must beEqualTo(140 - shortURLLength.get)

      }
    }
  }

}