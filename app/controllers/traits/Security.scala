package controllers.traits

import play.api._
import play.api.mvc._

import controllers._
import controllers.helpers._

trait Security {
    def onUnauthorized(implicit request: RequestHeader) = Results.Redirect(routes.Authentication.login).flashing("security" -> "You need to authenticate yourself to use this feature.")
    def onAuthorized(implicit request: RequestHeader) = Results.Redirect(routes.Application.index).flashing("security" -> "Authenticated user cannot access previous page.")

    def OnlyUnauthenticated(f: Request[AnyContent] => Result): Action[AnyContent] = Action { implicit request =>
        SessionHelper.isAuthenticated match {
            case true => onAuthorized
            case _ => f(request)
        }
    }
}