package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import anorm._

import models._
import views._

import controllers.traits.Security

object Administration extends Controller with Security {
    val siteInfoForm = Form(
        mapping(
            "id" -> ignored(NotAssigned: Pk[Long]),
            "title" -> nonEmptyText,
            "about" -> nonEmptyText
        )((id, title, about) => SiteInfo(id, title, about))((info: SiteInfo) => Some(info.id, info.title, info.about))
    )

    def updateSiteInfo = OnlyAuthenticated(Privilege.admin) { user =>
        implicit request =>
            SiteInfo.getInfo match {
                case Some(info: SiteInfo) => Ok(html.administration.updateSiteInfo(siteInfoForm.fill(info)))
                case _                    => InternalServerError
            }
    }

    def processSiteInfo = OnlyAuthenticated(Privilege.admin) { user =>
        implicit request =>
            siteInfoForm.bindFromRequest.fold(
                formWithErrors => BadRequest(html.administration.updateSiteInfo(formWithErrors)),
                siteInfo => {
                    try {
                        SiteInfo.update(siteInfo.title, siteInfo.about)
                        Redirect(routes.Application.index())
                    }
                    catch {
                        case e => InternalServerError
                    }
                }
            )
    }
}
