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

  lazy val shortURLLength = twitter.map { _.getAPIConfiguration().getShortURLLengthHttps() }

  def buildTweetFromEvent(eventToTweet: Event): String = {
    shortURLLength.map(buildTweetFromEvent(eventToTweet, _)).get
  }

  def buildTweetFromEvent(eventToTweet: Event, shortURLLength: Int): String = {
    val template = "N'oubliez pas, demain, c'est %s %s #wmit"

    val title = {
      val maxTitleSize = 140 - (template.size - 4) - shortURLLength
      compress(eventToTweet.title, maxTitleSize)
    }

    template.format(title, eventToTweet.url)
  }

  def sendTweet(eventToTweet: Event) = {
    twitter.map(_.updateStatus(buildTweetFromEvent(eventToTweet)))
  }

  private def compress(stringToSumUp: String, maxSize: Int) = {
    if (stringToSumUp.size <= maxSize) {
      stringToSumUp.trim
    } else {
      stringToSumUp.trim.substring(0, maxSize - 3) + "..."
    }

  }

}