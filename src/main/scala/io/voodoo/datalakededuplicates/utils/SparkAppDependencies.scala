package io.voodoo.datalakededuplicates.utils

import org.apache.spark.sql.SparkSession

trait SparkAppDependencies {
  def appName: String

  def getSparkSession(isLocal: Boolean): SparkSession = getSparkSession(isLocal, this.appName)

  def getSparkSession(isLocal: Boolean, name: String): SparkSession = {
    val spark =
      if (!isLocal)
        SparkSession.builder().appName(name).getOrCreate()
      else
        SparkSession.builder().master("local[*]").getOrCreate()

    spark.sparkContext.hadoopConfiguration.set("parquet.enable.summary-metadata", "false")
    spark.sparkContext.hadoopConfiguration.set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false")
    spark.conf.set("spark.sql.session.timeZone", "UTC")
    spark.sparkContext.setCheckpointDir("_checkpoints/")
    spark
  }
}
