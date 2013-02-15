package config

import play.api.Play

trait Config {

  def getPropertyFromConfOrEnvironment(name: String): Option[String] = {
    Play.current.configuration.getString(name).orElse(Some(sys.env(name)))
  }

}