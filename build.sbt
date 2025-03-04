ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
      name := "GoodsBasket",
      libraryDependencies ++= Seq(
          "com.typesafe.slick" %% "slick" % "3.5.2",  // Updated version
          "com.h2database" % "h2" % "2.3.232",
          "org.scalatest" %% "scalatest" % "3.2.19" % Test
      ),


  )