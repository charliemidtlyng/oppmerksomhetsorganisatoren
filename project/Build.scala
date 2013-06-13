import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "oppmerksomhetsorganisatoren"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "postgresql" % "postgresql" % "8.4-702.jdbc4",
    jdbc,
    anorm,
    "se.radley" %% "play-plugins-enumeration" % "1.1.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
