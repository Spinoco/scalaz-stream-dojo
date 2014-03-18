package com.spinoco.dojo

import scalaz.concurrent.Task
import scalaz.stream.Process
import Process._
/*
  Time to put it all together.

  So far, we have learned a few concepts about how to build and manipulate streams. We can use these simple concepts
  to build something more interesting. Lets imagine we have a process of events that indicate user actions. We want to build
  a service that will process these events and react to them.

  hint: you can use

  TODO given process of events from `userEvents`, create a process that will:
   - parse strings into case classes (you can use parseRequests helper function)
   - emit 'UserLoggedIn' if password from UserLogin is correct for given user AND user is not already logged in
      (you can use 'authenticate' function to check if password is correct)
   - emit 'UserLoggedOut' if and only if user already logged in

  hint: chances are, you will need a custom class that will represent state of the service with some helper methods so
    dont be afraid of that

  the result you should expect when your code is correct is defiend in val 'expectedResult'
 */

trait Request
case class UserLogin(userName: String, password: String) extends Request
case class UserLogout(userName: String) extends Request

trait Response
case class UserLoggedIn(userName: String) extends Response
case class UserLoggedOut(userName: String) extends Response

object DojoService {
  import Utils._
  def authenticate(username: String): Boolean = passwords.get(username).map(_ => true).getOrElse(false)
  val parseRequests: (String) => Request = parseRequestsHelper _
  val requests: Process[Task, String] = mockRequests

  val expectedResult = Vector(
    UserLoggedIn("lister"),
    UserLoggedIn("cat"),
    UserLoggedOut("lister"),
    UserLoggedIn("kryten"),
    UserLoggedOut("kryten"),
    UserLoggedOut("cat")
  )

  val parsedRequests: Process[Task, Request] = mockRequests.map(parseRequests)
  val userStateProcess: Process[Task, Response] = parsedRequests |> state(State(Set.empty))



  def state(init: State ): Process1[Request, Response] = await1[Request].flatMap { r =>
    val todo = r match {
      case r: UserLogin => init.tryLogin(r)
      case r: UserLogout => init.tryLogout(r)
    }
    emitSeq(todo._1) fby state(todo._2)
  }

  case class State(loggedUsers: Set[String]) {
    def tryLogin(req: UserLogin): (Seq[Response], State) = {
      if(loggedUsers.contains(req.userName)) {
        (List(), this)
      } else {
        if(authenticate(req.userName)) {
          (List(UserLoggedIn(req.userName)), State(loggedUsers + req.userName))
        } else {
          (List(), this)
        }
      }
    }

    def tryLogout(req: UserLogout): (Seq[Response], State) = {
      if(loggedUsers.contains(req.userName)) {
        (List(UserLoggedOut(req.userName)), State(loggedUsers - req.userName))
      } else {
        (List(), this)
      }
    }
  }

}