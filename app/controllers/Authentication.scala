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
    
    def signup = Action { implicit request =>
        Ok(html.authentication.signup(signupForm))
    }
    
    def signupProcess = Action { implicit request =>
        signupForm.bindFromRequest.fold (
            formWithErrors => BadRequest(html.authentication.signup(formWithErrors)),
            user => {
                try {
                	Users.create(user)
                	Redirect(routes.Application.index)
                } catch {
                    case e => {
                        val formWithErrors = signupForm.copy(errors=Seq(FormError("email", "Email already registered. Please recheck your email."))).fill(user)
                        BadRequest(html.authentication.signup(formWithErrors))
                    }
                }
            }
        )
    }
}