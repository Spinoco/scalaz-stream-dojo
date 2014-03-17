package com.spinoco.dojo

import scalaz.stream.Process
import scalaz.concurrent.Task

/**
 * Created by tomasherman on 16/03/14.
 */
object Main {


  def main(args: Array[String]) {
    val dojo = new Dojo {}
    import dojo._


    println(" >> Running process")
    //run main process, make sure the evaluation doesn't take longer than 10secs, otherwise throw TimeoutException

    def processToRun = sinkProcess

    val result = processToRun.runLog.timed(10000).run

    println(s" >> Process returned: $result")
    println(" QUITTING SBT")
    sys.exit(0)
  }
}
