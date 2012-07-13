package controllers

import play.api._
import play.api.mvc._

import controllers.helpers._
import controllers.traits._
import models._
import views._

object Post extends Controller with Security {
    def create = OnlyAuthenticated { user => implicit request =>
        Ok(html.post.create("POST FORM"))
    }
}