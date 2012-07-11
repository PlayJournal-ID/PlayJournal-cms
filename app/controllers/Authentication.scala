package controllers

import play.api._
import play.api.mvc._

import models._
import views._

object Authentication extends Controller {
    def signup = Action { implicit request =>
        Ok(html.authentication.signup("FORM"))
    }
    
    def signupProcess = Action { implicit request =>
        Ok(html.authentication.signup("FORM"))
    }
}