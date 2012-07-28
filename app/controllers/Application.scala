package controllers

import play.api._
import play.api.mvc._

import models._
import models.extra.Page

object Application extends Controller {

    def index(page: Long) = Action { implicit request =>
        val posts = models.Post.getPageItem(page, 5)
        Ok(views.html.index(posts))
    }

    def about = Action { implicit request =>
        val about = SiteInfo.about
        Ok(views.html.about(about))
    }

}