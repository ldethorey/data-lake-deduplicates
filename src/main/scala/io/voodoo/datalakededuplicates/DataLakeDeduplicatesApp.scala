package io.voodoo.datalakededuplicates

import io.voodoo.datalakededuplicates.utils.{AppArguments, AppEnvironment, ExitCodes, SparkAppDependencies}
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.sql.SparkSession

import java.io.{PrintWriter, StringWriter}
import scala.util.{Failure, Success, Try}

object DataLakeDeduplicatesApp extends SparkAppDependencies {

  override def appName: String = this.getClass.getCanonicalName.dropRight(1)
  private val logger = LogManager.getLogger(this.getClass)
  logger.setLevel(Level.INFO)

  def main(args: Array[String]): Unit = {
    println(s"Running $appName")

    println("Reading arguments")
    val configArgs = AppArguments.parser(appName = appName, arguments = args) match {
      case Some(config) => config
      case _ =>
        System.err.println(AppArguments.getErrorString)
        sys.exit(ExitCodes.ERROR_ARG_CONF)
    }
    println(configArgs)

    val configEnv = AppEnvironment.loadEnvironmentVariables() match {
      case Success(p) => p
      case Failure(ex) =>
        System.err.println(ex.getMessage)
        sys.exit(ExitCodes.ERROR_PARSING_CONF)
    }

    implicit val spark: SparkSession = this.getSparkSession(configEnv.isLocal, appName)

    val stepAttempt = Try(step(configArgs, configEnv))

    stepAttempt match {

      case Success(_) =>
        logger.info(s"Job AdjustNormalizationApp succeed")

      case Failure(f) =>
        logger.error("Job AdjustNormalizationApp failed")
        logger.error(f.getStackTrace)

        val sw = new StringWriter
        f.printStackTrace(new PrintWriter(sw))
        sw.toString
        throw f
    }
  }

  def step(configArgs: AppArguments, configEnv: AppEnvironment)(implicit spark: SparkSession): Unit = {


  }


}
