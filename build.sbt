




  val appName         = "twivent"
  val appVersion      = "1.0-SNAPSHOT"

  scalaVersion := "2.10.4"


  resolvers += "google-api-services" at "http://google-api-client-libraries.appspot.com/mavenrepo"
  resolvers += Resolver.url("Autoping repository", url("http://juliender.github.io/autoping-play2-plugin/snapshots/"))(Resolver.ivyStylePatterns)


  libraryDependencies ++= Seq(
    "com.google.apis" % "google-api-services-calendar" % "v3-rev111-1.19.0", // exclude com.google.guava#guava-jdk5;13.0!guava-jdk5.jar ?
    "com.google.http-client" % "google-http-client-jackson2" % "1.19.0",
    "org.twitter4j" % "twitter4j-core" % "3.0.5",
    "com.github.ndeverge" %% "autoping-play2-plugin" % "0.1.2"

  )



lazy val root = (project in file(".")).enablePlugins(PlayScala)
