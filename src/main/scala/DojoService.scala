package com.spinoco.dojo

import scalaz.concurrent.Task
import scalaz.stream.{process1, Process}
import Process._
import scala.concurrent.duration._

/*
  Time to put it all together.

  So far, we have learned a few concepts about how to build and manipulate streams. We can use these simple concepts
  to build something more interesting. Lets imagine we have a process of events that indicate user actions. We want to build
  a service that will process these events and react to them.

  TODO given process of events from `userEvents`, create a process that will:
   - parse strings into case classes (you can use parseRequests helper function)
   - emit 'UserLoggedIn' if password from UserLogin is correct for given user AND user is not already logged in
      (you can use 'authenticate' function to check if password is correct)
   - emit 'UserLoggedOut' if and only if user already logged in

  the result you should expect when your code is correct is defiend in val 'expectedResult'

  When you are done, what you can do is to use `fileWithRequests` instead of `requests` as input for your service. There
  are some helper functions in scalaz library for working with input streams that might help you out. Notice how little code you
  have to change to swap things around.
 */

trait Request
case class UserLogin(userName: String, password: String) extends Request
case class UserLogout(userName: String) extends Request

trait Response
case class UserLoggedIn(userName: String) extends Response
case class UserLoggedOut(userName: String) extends Response

object DojoService {
  import Utils._
  def authenticate(username: String, pass: String): Boolean = passwords.get(username).map(_ == pass).getOrElse(false)
  val parseRequests: (String) => Request = parseRequestsHelper _
  val fileWithRequests = this.getClass.getResourceAsStream("/testData.txt")
  val requests: Process[Task, String] = mockRequests

  val expectedResult = Vector(
    UserLoggedIn("lister"),
    UserLoggedIn("cat"),
    UserLoggedOut("lister"),
    UserLoggedIn("kryten"),
    UserLoggedOut("kryten"),
    UserLoggedOut("cat")
  )

  def parsedRequests: Process[Task, Request] = ???

  def userStateProcess: Process[Task, Response] = ???

}