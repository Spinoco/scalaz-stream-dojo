package com.spinoco.dojo

/**
 * Created by tomasherman on 16/03/14.
 */
object Main extends App {

  import Dojo._
  import DojoService._


  println(" >> Running process")
  def processToRun = userStateProcess

  val result = processToRun.runLog.run

  println(s" >> Process returned: $result")
}
