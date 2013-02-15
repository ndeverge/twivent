package notifier

import org.specs2.mutable.Specification
import twitter4j.conf.ConfigurationBuilder
import twitter4j.TwitterFactory
import twitter4j.Status
import play.api.Play
import play.api.test.FakeApplication.apply
import play.api.test.Helpers.running
import play.api.test.FakeApplication

class TwitterSpec extends Specification {

  "Twitter API" should {
    "be able to auth" in {

      running(FakeApplication()) {
        Twitter.twitter must beSome

        Twitter.twitter.get.getScreenName() must not beNull
      }
    }
  }

}