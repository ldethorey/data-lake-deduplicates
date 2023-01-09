package io.voodoo.datalakededuplicates.utils
import scala.util.Try


case class AppEnvironment(isLocal: Boolean = false,
                          env: String,
                          emrClusterId: String,
                          jobId: String
                         )

object AppEnvironment {

  def loadEnvironmentVariables(): Try[AppEnvironment] = Try {
    // environment
    val isLocal = Try(sys.env("LOCAL").toBoolean).toOption.getOrElse(false)
    val environment = sys.env.getOrElse("ENV", "local")

    // EMR
    val emrClusterId = sys.env.getOrElse("EMR_CLUSTER_ID", "none")
    val jobId = sys.env.getOrElse("JOB_RUN_ID", "none")

    AppEnvironment(
      isLocal = isLocal,
      env = environment,
      emrClusterId = emrClusterId,
      jobId = jobId
    )
  }
}
