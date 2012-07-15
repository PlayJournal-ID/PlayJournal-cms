package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {

    def index(page: Long) = Action { implicit request =>
        val posts = models.Post.findFrontPage(page)
        Ok(views.html.index("PlayJournal CMS"))
    }

}