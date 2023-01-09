name := "data-lake-deduplicates"
version := "1.0.2"
scalaVersion := "2.12.10"

cancelable in Global := true // allow sbt to interrupt the task with Ctrl-C
assembly / mainClass:= Some("io.voodoo.data_lake_deduplicates.App")

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "io.voodoo.adjustnormalization",
  )

credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  System.getenv("GITHUB_USERNAME"),
  System.getenv("GITHUB_TOKEN")
)

// Dependencies Versioning
lazy val Versions = new {
  // this dependency is not in mvnrepository
  val scalactic = "3.2.12" // Allows === and !== operators
  val scalatest = "3.2.12"

  // AWS EMR 6.7.0 has spark 3.1.2 and Hadoop 2.8.5
  val spark = "3.2.1"
  val hadoop = "3.3.2"
  val aws = "1.12.239"

  // spark scala test
  val sparkTests = "1.3.0"

  // JSON Serializer
  val spray = "1.3.6"

  // HTTP queries
  val scalaj = "2.4.2"

  // https://mvnrepository.com/artifact/com.github.scopt/scopt
  val scopt = "4.0.1"
}

resolvers ++= Seq(
  "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven",
  "github_aws2scala" at s"https://maven.pkg.github.com/VoodooTeam/aws2scala"
)

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % Versions.scalactic,
  "org.scalatest" %% "scalatest" % Versions.scalatest % Test,
  "org.apache.spark" %% "spark-core" % Versions.spark % "provided" exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.spark" %% "spark-sql" % Versions.spark % "provided",
  "org.apache.spark" %% "spark-hive" % Versions.spark % "provided",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.3",
  "com.github.mrpowers" %% "spark-fast-tests" % Versions.sparkTests % Test,
  "org.apache.hadoop" % "hadoop-client" % Versions.hadoop % "provided",
  "org.apache.hadoop" % "hadoop-aws" % Versions.hadoop % "provided",
  "com.amazonaws" % "aws-java-sdk-s3" % Versions.aws,
  "com.amazonaws" % "aws-java-sdk-cloudwatch" % Versions.aws,
  "com.amazonaws" % "aws-java-sdk-glue" % Versions.aws,
  "com.amazonaws" % "aws-java-sdk-logs" % Versions.aws,
  "com.amazonaws" % "aws-java-sdk-sts" % Versions.aws,
  "io.spray" %% "spray-json" % Versions.spray,
  "org.scalaj" %% "scalaj-http" % Versions.scalaj,
  "com.github.scopt" %% "scopt" % Versions.scopt,
  "io.voodoo" % "aws2scala" % "1.4.13"
)

// remove duplicates
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

// Build version number is based on the GIT tags using the sbt-git plugin
enablePlugins(GitVersioning)
git.gitTagToVersionNumber := { tag: String =>
  if(tag matches "[0-9]+\\..*") Some(tag)
  else None
}
