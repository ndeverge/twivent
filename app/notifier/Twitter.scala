package notifier

import twitter4j.conf.ConfigurationBuilder
import twitter4j.TwitterFactory
import calendar.Event

object Twitter extends config.Config {

  lazy val twitterConfigurationBuilder = {
    getPropertyFromConfOrEnvironment("twitter.consumerKey").flatMap {
      consumerKey =>
        getPropertyFromConfOrEnvironment("twitter.consumerSecret").flatMap {
          consumerSecret =>
            getPropertyFromConfOrEnvironment("twitter.accessToken").flatMap {
              accessToken =>
                getPropertyFromConfOrEnvironment("twitter.accessTokenSecret").flatMap {
                  accessTokenSecret =>
                    Some(new ConfigurationBuilder().setDebugEnabled(true)
                      .setOAuthConsumerKey(consumerKey)
                      .setOAuthConsumerSecret(consumerSecret)
                      .setOAuthAccessToken(accessToken)
                      .setOAuthAccessTokenSecret(accessTokenSecret))
                }
            }
        }
    }
  }
  lazy val twitterFactory = twitterConfigurationBuilder.map { confBuilder => new TwitterFactory(confBuilder.build()) }

  lazy val twitter = twitterFactory.map { factory => factory.getInstance() }

  //twitter.get.getAPIConfiguration().getShortURLLength()

  def buildTweetFromEvent(eventToTweet: Event): String = {
    s"Demain $eventToTweet.title #wmit"
  }

}