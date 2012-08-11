package controllers.traits

import play.api._
import play.api.mvc._

import controllers._
import controllers.helpers._

trait Security {
    def username(request: RequestHeader) = SessionHelper.getUserName(request)

    def onUnauthenticated(request: RequestHeader) = Results.Redirect(routes.Authentication.login).flashing("security" -> "You need to authenticate yourself to use this feature.")
    def onAuthenticated(request: RequestHeader) = Results.Redirect(routes.Application.index()).flashing("security" -> "Authenticated user cannot access previous page.")

    def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.index()).flashing("security" -> "You are not authorized to access previous page.")

    def OnlyUnauthenticated(f: Request[AnyContent] => Result): Action[AnyContent] = Action { implicit request =>
        SessionHelper.isAuthenticated match {
            case true => onAuthenticated(request)
            case _    => f(request)
        }
    }

    def OnlyAuthenticated(privilege: Long*)(f: => String => Request[AnyContent] => Result) = {
        Security.Authenticated(username, onUnauthenticated) { user =>
            Action { implicit request =>
                (privilege.contains(SessionHelper.getUserPrivilege)) match {
                    case true  => f(user)(request)
                    case false => onUnauthorized(request)
                }
            }
        }
    }

    def OnlyAuthenticated(f: => String => Request[AnyContent] => Result) = {
        Security.Authenticated(username, onUnauthenticated) { user =>
            Action(implicit request => f(user)(request))
        }
    }
}