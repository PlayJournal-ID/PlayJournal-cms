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

object Post extends Controller with Security {
    val postForm = Form(
        mapping(
            "id" -> ignored(NotAssigned:Pk[Long]),
            "title" -> nonEmptyText,
            "content" -> nonEmptyText
        ) ((id, title, content) => models.Post(id, title, content))
          ((post: models.Post) => Some(post.id, post.title, post.content))
    )
    
    def create = OnlyAuthenticated { user => implicit request =>
        Ok(html.post.create(postForm))
    }
    
    def createPost = OnlyAuthenticated { user => implicit requset =>
        Ok(html.post.create(postForm))
    }
}