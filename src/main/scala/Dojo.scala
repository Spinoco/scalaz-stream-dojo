package com.spinoco.dojo

import scalaz.stream.Process
import Process._
import scalaz.concurrent.Task
import scala.util.Random

object Dojo extends Dojo

trait Dojo {

  /*
    The most basic type of process is one that will emit single element and then halts.
    You can create it using the `emit` (or the `emitSeq` variant) function:

    emit has following type: emit[O](o: O): Process[Nothing, O]

    however, we will often need process of type Process[Task, O]. To do that, you can
    use .toSource method which will return same process, but with correct type signature.

    TODO create a process that will emit a string

    you can run the process in the Main.scala
   */

  def simpleStream: Process[Task, String] = ???


  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  /*
    Ok, now you know how to emit a message. Now lets take a look at how we can do something useful with those messages.

    Once we have a process p: Process[Task, O], we can use method `map` and to transform messages.

     TODO using 'sampleInput' to create a stream of strings
   */

  val sampleInput = emitSeq(List.fill(10)(Random.nextInt())).toSource

  def stringStream: Process[Task, String] = ??? //sampleInput.map(_.toString) //???

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  /*
    Cool. Now lets assume we want to take all the inputs from `sampleInput2` defined below, as long as the
    values are non-negative. In other words, we want emit messages until we find one below 0, then we want to halt the stream.

    Use flatMap for that. To halt the stream you can use `halt` helper function.

    TODO using 'sampleInput2' create a stream that will emit all positive or zero messages until first negative
   */

  val sampleInput2 = emitSeq(List(1,2,3,0,2,-1,1,2)).toSource

  def positiveStream = ???

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  /*
    Ok, until now, we have been simply modifying streams. Now lets learn how to compose stream.

    You can take a process and run it's content through a `pipe` method (or it's alias `|>`), which takes something called 'process1'.

    Process1 takes two type parameters, I and O, and it basically is a transformation from I to O.

    To create a process1, you can use method await1[A], which creates a Process1[A,A]. All it does is that it awaits for 1 value.
    It has a map and flatMap method which gives you the ability to transform the accumulated I into O.

    By default, process1 halts after receiving 1 element. You can, however, use it's repeat method to make it run as long as there
     are any input values available.

    TODO create a process1 that will await int and emit square. Then make it repeatable. Then pipe messages from 'sampleInput3' into that process1
   */

  val sampleInput3 = emitSeq((0 to 10).toList).toSource

  def p1Squares: Process1[Int,Int] = ???
  def p1SquaresRepeated: Process1[Int, Int] = ???
  def squaresStream = ???

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*
    Now that we know about process1 and piping, we should learn about another cool thing about processes. It is possible to
    define process1 by composing two processes1. For example, given two processes p1 and p2, you can define new process that
     will do whatever p1 does and once p1 halts, it will do whatever p2 does. You can use `fby` (short for 'follow by') operator
     on process1 to implement this behavior.

    This is very powerful as we will see later when implementing DojoService.

     TODO create process1 `p` such that it will emit ints as long as they are positive and halts upon receiving first negative one
     TODO make sure you use fby combinator, don't use repeat

  */

  val sampleInput4 = emitSeq(List(1,2,3,4,-1,-2,-3,1)).toSource
  def p: Process1[Int, Int] = ???

  def combinedProcesses = ???

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*
    The pattern you came up with above is very powerful. You can use this approach to create processes that
    contain a state.

    hint: This might not be quite as trivial as the examples above. You might need some sort of a inner function and recursion.

    TODO create a `indexer` process1 that will wrap every message that passes through it with an index of the message.

    for example for input List("one", "two", "three") we will get following output: Vector((0,"one"), (1, "two"), (2, "three"))
   */
  val sampleInput5 = emitSeq(List("uno", "dos", "tres", "cuatro", "cinco", "seis")).toSource

  def indexer: Process1[String, (Int, String)] = ???

  def statefulProcess1 = ???

  /*

    TODO continue in the file "DojoService.scala"

   */
}


