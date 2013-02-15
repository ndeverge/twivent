package twitter

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
    "send a tweet" in {

      running(FakeApplication()) {
        val twitterConfigurationBuilder = new ConfigurationBuilder();
        twitterConfigurationBuilder.setDebugEnabled(true)
          .setOAuthConsumerKey(Play.current.configuration.getString("twitter.consumerKey").get)
          .setOAuthConsumerSecret(Play.current.configuration.getString("twitter.consumerSecret").get)
          .setOAuthAccessToken(Play.current.configuration.getString("twitter.accessToken").get)
          .setOAuthAccessTokenSecret(Play.current.configuration.getString("twitter.accessTokenSecret").get);
        val twitterFactory = new TwitterFactory(twitterConfigurationBuilder.build());
        val twitter = twitterFactory.getInstance();
        val status = twitter.updateStatus("This is my first tweet")
      }
    }
  }

}