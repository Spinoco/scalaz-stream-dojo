package com.spinoco.dojo

import scalaz.stream.{process1, Process}
import Process._
import scalaz.concurrent.Task
import scala.util.Random
import scala.collection.mutable.ListBuffer

/**
 * Created by tomasherman on 16/03/14.
 */
trait Dojo {
  type Res = String

  /*
    The most basic type of process is one that will emit single element and then halts.
    You can create it using the `emit` (or the `emitSeq` variant) function:

    emit has following type: emit[O](o: O): Process[Nothing, O]

    however, we will often need process of type Process[Task, O]. To do that, you can
    use .toSource method which will return same process, but with correct type signature.

    TODO create a process that will emit a string

    you can run the process in the Main.scala
   */

  val simpleStream: Process[Task, String] = emit("hi mom").toSource

  /*
    Ok, now you know how to emit a message. Now lets take a look at how we can do something useful with those messages.

    Once we have a process p: Process[Task, O], we can use method `map` and to transform messages.

     TODO using 'sampleInput' to create a stream of strings
   */

  val sampleInput = emitSeq(List.fill(10)(Random.nextInt())).toSource

  val stringStream: Process[Task, String] = sampleInput.map(_.toString)

  /*
    Cool. Now lets assume we want to take all the inputs from `sampleInput2` defined below, as long as the
    values are non-negative. In other words, we want to halt the process if find a value below 0.

    Use flatMap for that. To halt the stream you can use `halt` helper function.

    TODO using 'sampleInput2' create a stream that will all positive or zero messages until first negative
   */

  val sampleInput2 = emitSeq(List(1,2,3,0,2,-1,1,2)).toSource

  val positiveStream = sampleInput2.flatMap { a =>
    if (a < 0) halt else emit(a)
  }

  /*
    Ok, until now, we have been simply modifying streams we already have. Now lets learn how to compose stream.

    You can take a process and run it's content through a `pipe` method (or it's alias `|>`), which takes something called 'process1'.

    Process1 takes two type parameters, I and O, and it basically is a transformation from I to O.

    To create a process1, you can use method await[A], which creates a Process1[A,A]. All it does is that it awaits for 1 value.
    It has a map and flatmap method which you can use to do fun stuff with the value that was accumulated in the givne await.

    By default, process1 halts after receiving 1 element. You can, however, use it's repeat method to make it run as long as there
     are any input values available.



    TODO create a process1 that will await ints and emit squares. Then pipe messages from 'sampleInput3' into that process1
   */

  val sampleInput3 = emitSeq((0 to 10).toList).toSource

  val p1Squares: Process1[Int,Int] = await1.map(a => a*a)

  val squaresStream = sampleInput3 |> p1Squares.repeat

  /*
    Now that we know about process1 and piping, we should learn about another cool thing about processes. It is possible to
    define process1 by composing two processes1. For example, given two processes p1 and p2, you can define new process that
     will do whatever p1 does and once p1 halts, it will do whatever p2 does. You can use `fby` (short for follow by) operator
     on process1 to implement this behavior.

    This is very powerful, and later on you will see how we can use this to implement pretty cool things.

     TODO create process1 `p` such that it will emit ints as long as they are positive and halts upon receiving first negative one
     TODO make sure you use fby combinator

     hint: recursion is your friend

   */

  val sampleInput4 = emitSeq(List(1,2,3,4,-1,-2,-3,1)).toSource
  val p: Process1[Int, Int] = await1[Int].flatMap{ a => if (a > 0) { emit(a) fby p } else { halt }}

  val combinedProcesses = sampleInput4 |> p

  /*
    Guess what, the pattern you came up with above is very powerful. You can use this approach to create processes that
    contain a state.

    hint: This might not be quite as trivial as the examples above. You might need some sort of a inner function and recursion.

    TODO create a `indexer` process1 that will wrap every message that passes through it with an index of the message.

    for example for input List("one", "two", "three") we will get following output: Vector((0,"one"), (1, "two"), (2, "three"))
   */
  val sampleInput5 = emitSeq(List("uno", "dos", "tres", "cuatro", "cinco", "seis")).toSource

  def indexer: Process1[String, (Int, String)] = {
    def go(state: Int): Process1[String, (Int, String)] = {
      await1[String].flatMap { s =>
        emit(state -> s) fby go(state + 1)
      }
    }
    go(0)
  }

  val statefulProcess1 = sampleInput5 |> indexer

  /*
    We are finally getting somewhere. We will now discuss one last topic before we get to the good stuff.
    All we did with the results of all of our processes was we accumulated them and then printed them out. But
    we can do more with them. We might want to store them into a database for example. Thats what sinks are for.

    Sink[Task, A] is basically a process, that emits function A => Task[Unit]. What that means, is that it provides
     functions that will run some sort of effect and return nothing. It's really as simple as that

    To use sink, simply you can use `to` method.

    TODO create a sink that will append elements into a list buffer

    (this is a little grose, but it's ok for the sake of example. You can be sure that you won't find any list buffers in
    our codebase, though ;) )

    hint: you can use `constant` process to implement this excersise
    hint: use Task.now to wrap any value into Task monad
   */

  val sampleInput6 = emitSeq("nobody expects the spanish inquisition".split(" ")).toSource
  val listBuffer = ListBuffer.empty[String]
  val sink: Sink[Task, String] = constant[String => Task[Unit]] {
    s => Task.now { listBuffer.append(s) }
  }

  val sinkProcess = (sampleInput6 to sink).drain fby eval_(Task.delay{ println(" content of listBuffer" + listBuffer)})




}
