package com.spinoco.dojo

import scalaz.concurrent.Task
import scalaz.stream._

object Utils {
  import Dojo._
  val passwords = Map(
    "lister" -> "you",
    "rimmer" -> "can",
    "cat" -> "not",
    "kryten" -> "pass"
  )


  def mockRequests = Process.emitSeq(List(
    "login|lister|you",
    "login|cat|not",
    "logout|lister",
    "logout|lister",
    "login|cat|not",
    "login|kryten|que?",
    "login|polymorphe|himom!",
    "login|kryten|pass",
    "logout|kryten",
    "logout|cat"
  )).toSource



  def echo[A](prefix: String): Sink[Task, A] = Process.constant[A => Task[Unit]]({
    a: A => Task.now {println(s"$prefix -> $a")}
  })

  def parseRequestsHelper(str: String): Request = {
    str.split('|').toSeq.toList match {
      case "login" :: uname :: pass :: Nil => UserLogin(uname, pass)
      case "logout" :: uname :: Nil => UserLogout(uname)
      case x => throw new Exception("parsing failed for: " +  x)
    }
  }



}
