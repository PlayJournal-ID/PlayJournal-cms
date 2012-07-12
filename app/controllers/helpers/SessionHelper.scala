package controllers.helpers

import play.api._
import play.api.mvc._

object SessionHelper {
    def createSession(id: Long, email: String, privilege: Long) = Array(("id" -> id.toString), (Security.username -> email), ("privilege" -> privilege.toString))
    
    def isAuthenticated(implicit request: RequestHeader) = request.session.get(Security.username).exists(_ => true)
    
    def getUserName(implicit request: RequestHeader) = request.session.get(Security.username)
}