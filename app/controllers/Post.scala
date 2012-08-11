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
import models.extra.Page
import views._

object Post extends Controller with Security {
    val postForm = Form(
        mapping(
            "id" -> ignored(NotAssigned: Pk[Long]),
            "title" -> nonEmptyText,
            "content" -> nonEmptyText
        )((id, title, content) => models.Post(id, title, content))((post: models.Post) => Some(post.id, post.title, post.content))
    )

    def list(page: Long) = OnlyAuthenticated(Privilege.admin) { user =>
        implicit request =>
            val posts = models.Post.getPostByWriterPaginated(SessionHelper.getUserId, page, 5)
            Ok(html.post.list(posts))
    }

    def show(id: Long, title: String = "") = Action { implicit request =>
        models.Post.findById(id) match {
            case Some(post: models.Post) => Ok(html.post.show(post))
            case _                       => NotFound
        }
    }

    def create = OnlyAuthenticated(Privilege.admin, Privilege.editor, Privilege.writer) { user =>
        implicit request =>
            Ok(html.post.create(postForm))
    }

    def edit(id: Long) = OnlyAuthenticated(Privilege.admin, Privilege.editor, Privilege.writer) { user =>
        implicit request =>
            models.Post.findById(id) match {
                case Some(post: models.Post) => {
                    if (post.writer == SessionHelper.getUserId) {
                        Ok(html.post.edit(postForm.fill(post), id))
                    }
                    else {
                        Redirect(routes.Post.show(id, post.titleSlug)).flashing("security" -> "You are not allowed to edit this post")
                    }
                }
                case _ => NotFound
            }
    }

    def createPost(implicit request: RequestHeader, httpRequest: play.api.mvc.Request[_]) = {
        postForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.post.create(formWithErrors)),
            post => {
                try {
                    val userId: Long = SessionHelper.getUserId
                    val newPost = models.Post.create(post.title, post.content, userId)
                    Redirect(routes.Post.show(newPost.getOrElse(0), post.titleSlug))
                }
                catch {
                    case e => {
                        // global error == error without a key
                        val formWithErrors = postForm.copy(errors = Seq(FormError("", "Ooops. We get an error creating your post. Please relogin and try again."))).fill(post)
                        BadRequest(html.post.create(formWithErrors))

                    }
                }
            }
        )
    }

    def editPost(id: Long)(implicit request: RequestHeader, httpRequest: play.api.mvc.Request[_]) = {
        postForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.post.edit(formWithErrors, id)),
            post => {
                try {
                    models.Post.update(id, post.title, post.content)
                    Redirect(routes.Post.show(id, post.titleSlug))
                }
                catch {
                    case e => {
                        val formWithErrors = postForm.copy(errors = Seq(FormError("", "Ooops. We get an error creating your post. Please relogin and try again."))).fill(post)
                        BadRequest(html.post.create(formWithErrors))
                    }
                }
            }
        )
    }

    def processPostForm(id: Long) = OnlyAuthenticated(Privilege.admin, Privilege.editor, Privilege.writer) { user =>
        implicit request =>
            val referer = request.headers.get("referer").getOrElse("")

            if (referer.contains("post/new")) createPost
            else if (referer.contains("post/edit")) editPost(id)
            else Forbidden
    }
}