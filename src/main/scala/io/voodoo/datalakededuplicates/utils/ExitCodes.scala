package io.voodoo.datalakededuplicates.utils

object ExitCodes extends Enumeration {
  type ExitCode = Int

  val OK: ExitCode = 0
  val INPUT_NOT_FOUND: ExitCode = 1

  val ERROR: ExitCode = -1
  val ERROR_ARG_CONF: ExitCode = -2
  val ERROR_PARSING_CONF: ExitCode = -3
  val ERROR_SPARK_SAVING: ExitCode = -4
  val ERROR_ENV_CONF: ExitCode = -5
  val ERROR_APP_CONF: ExitCode = -6

}
