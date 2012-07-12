package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import anorm._

import models._
import views._

object Authentication extends Controller {
    val signupForm = Form(
        mapping(
            "id" -> ignored(NotAssigned:Pk[Long]),
            "email" -> email.verifying(nonEmpty),
            "password" -> nonEmptyText,
            "name" -> nonEmptyText
        ) ((id, email, password, name) => Users(id, email, password, name))
          ((user: Users) => Some(user.id, user.email, user.password, user.name))
    )
    
    val loginForm = Form(
        tuple(
            "email" -> email.verifying(nonEmpty),
            "password" -> nonEmptyText
        ) verifying ("Email and password do not match.", result => result match {
            case (email, password) => Users.authenticate(email, password).isDefined
        })
    )
    
    def signup = Action { implicit request =>
        Ok(html.authentication.signup(signupForm))
    }
    
    def signupProcess = Action { implicit request =>
        signupForm.bindFromRequest.fold (
            formWithErrors => BadRequest(html.authentication.signup(formWithErrors)),
            user => {
                try {
                	Users.create(user)
                	Redirect(routes.Authentication.login).flashing("registration" -> "Your account have been created. You can now login and start using PlayJournal.")
                } catch {
                    case e => {
                        val formWithErrors = signupForm.copy(errors=Seq(FormError("email", "Email already registered. Please recheck your email."))).fill(user)
                        BadRequest(html.authentication.signup(formWithErrors))
                    }
                }
            }
        )
    }
    
    def login = Action { implicit request => 
        Ok(html.authentication.login(loginForm))
    }
    
    def authenticate = Action { implicit request =>
        loginForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.authentication.login(formWithErrors)),
            user => Redirect(routes.Application.index)
        )
    }
}