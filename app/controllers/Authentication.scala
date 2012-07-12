package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import anorm._

import controllers.helpers._
import controllers.traits._
import models._
import views._

object Authentication extends Controller with Security {
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
    
    def signup = OnlyUnauthenticated { implicit request =>
        Ok(html.authentication.signup(signupForm))
    }
    
    def signupProcess = OnlyUnauthenticated { implicit request =>
        signupForm.bindFromRequest.fold (
            formWithErrors => BadRequest(html.authentication.signup(formWithErrors)),
            user => {
                // there will be an exception if email's already in database, hence the try-catch block.
                // Anorm might be leaking abstraction here. Note this for future update.
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
    
    def login = OnlyUnauthenticated { implicit request => 
        Ok(html.authentication.login(loginForm))
    }
    
    def authenticate = OnlyUnauthenticated { implicit request =>
        loginForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.authentication.login(formWithErrors)),
            loginInfo => {
                val user: Users = Users.findByEmail(loginInfo._1) match {
                    case Some(u) => u
                    // there should be no wrong email by now. It has been authenticated by Users.authenticate before!
                    case None => throw new Exception("FATAL ERROR: Authentication.Authenticate get the wrong email.")
                }
                
            	Redirect(routes.Application.index).withSession(SessionHelper.createSession(user.id.get, user.email, user.privilege): _*)
            }
        )
    }
    
    def logout = Action { implicit request =>
        Redirect(routes.Application.index).withNewSession
        	.flashing("logoutSuccess" -> "Thank you for using PlayJournal!")
    }
}