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
    
    def show(id: Long) = Action { implicit request =>
        models.Post.findById(id) match {
            case Some(post) => Ok(html.post.show(post))
            case _ => Redirect(routes.Application.index)
        }
    }
    
    def create = OnlyAuthenticated { user => implicit request =>
        Ok(html.post.create(postForm))
    }
    
    def createPost = OnlyAuthenticated { user => implicit requset =>
        postForm.bindFromRequest.fold (
            formWithErrors => BadRequest(html.post.create(formWithErrors)),
            post => {
                try {
                    val userId: Long = SessionHelper.getUserId
                    val newPost = models.Post.create(post.title, post.content, userId)
                    Redirect(routes.Post.show(newPost.getOrElse(0))) // getOrElse should never be called.
                } catch {
                    case e => {
                        // global error == error without a key
                        val formWithErrors = postForm.copy(errors=Seq(FormError("", "Ooops. We get an error creating your post. Please relogin and try again."))).fill(post)
                        BadRequest(html.post.create(formWithErrors))
                    }
                }
            }
        )
    }
}