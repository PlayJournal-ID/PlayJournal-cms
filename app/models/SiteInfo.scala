package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import play.api.Play.current

case class SiteInfo(id: Pk[Long], title: String, about: String)

object SiteInfo {
    val simple = {
        get[Pk[Long]]("siteinfo.id") ~
        get[String]("siteinfo.title") ~
        get[String]("siteinfo.about") map {
            case id~title~about => SiteInfo(id, title, about)
        }
    }

    def getInfo: Option[SiteInfo] = {
        DB.withConnection{ implicit connection =>
            SQL("SELECT * FROM siteinfo WHERE siteinfo.id = 1")
                .as(simple.singleOpt)
        }
    }

    def about: String = {
        DB.withConnection{ implicit connection =>
            // there's only one instance of this table, so id is always 1
            SQL("SELECT si.about FROM siteinfo as si WHERE si.id = 1")
                .as(scalar[String].single)
        }
    }

    def title: String = {
        DB.withConnection{ implicit connection =>
            SQL("SELECT si.title FROM siteinfo as si WHERE si.id = 1")
                .as(scalar[String].single)
        }
    }

    def create(title: String, about: String) = {
        DB.withConnection{ implicit connection =>
            SQL("INSERT INTO siteinfo(title, about) VALUES ({title}, {about})")
                .on('title -> title, 'about -> about)
                .executeInsert()
        }
    }

    def update(title: String, about: String) = {
        DB.withConnection{ implicit connection =>
            SQL("UPDATE siteinfo SET title = {title}, about = {about} WHERE id = 1")
                .on('title -> title, 'about -> about)
                .executeUpdate()
        }
    }
}
