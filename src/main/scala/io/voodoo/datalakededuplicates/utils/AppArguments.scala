package io.voodoo.datalakededuplicates.utils
import scopt.{DefaultOParserSetup, OParser, OParserBuilder, OParserSetup}
import sourcecode.Text.generate

import java.io.ByteArrayOutputStream

case class AppArguments()

object AppArguments {

  val outCapture = new ByteArrayOutputStream
  val errCapture = new ByteArrayOutputStream

  val setup: OParserSetup = new DefaultOParserSetup {
    override def showUsageOnError: Some[Boolean] = Some(true)

    override def errorOnUnknownArgument = false
  }

  val builder: OParserBuilder[AppArguments] = OParser.builder[AppArguments]

  def parser(appName: String): OParser[Unit, AppArguments] = {
    import builder._
    OParser.sequence(
      programName(appName),

      // head
      head(appName, "1"),

      // arguments
      opt[String]("partitions-list-path")
        .required()
        .action((x, c) => c.copy(partitionsListPath = x))
        .text("bucket is a string accepting s3 url format"),
    )
  }

  def getOutputString: String = outCapture.toString

  def getErrorString: String = errCapture.toString

  def parser(appName: String, arguments: Array[String]): Option[AppArguments] = {
    Console.withOut(outCapture) {
      Console.withErr(errCapture) {
        OParser.parse(AppArguments.parser(appName), arguments, AppArguments(), setup)
      }
    }
  }
}
