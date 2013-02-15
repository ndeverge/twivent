package notifier

import twitter4j.conf.ConfigurationBuilder
import twitter4j.TwitterFactory

object Twitter extends config.Config {

  val twitterConfigurationBuilder = {
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
  val twitterFactory = twitterConfigurationBuilder.map { confBuilder => new TwitterFactory(confBuilder.build()) }

  val twitter = twitterFactory.map { factory => factory.getInstance() }

}