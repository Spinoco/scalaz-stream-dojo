package com.spinoco.dojo

object Main extends App {

  import Dojo._
  import DojoService._
  println(" >> Running process")

  //here you can run your processes by changing the function that's being called
  def processToRun = simpleStream

  val result = processToRun.runLog.run
  println(s" >> Process returned: $result")
}
