import play.api._
import models._
import anorm._

object Global extends GlobalSettings {
    override def onStart(app: Application) = {
        if (Privilege.findAll.isEmpty) {
            Seq(
                Privilege(Id(1), "admin"),
                Privilege(Id(2), "standard"),
                Privilege(Id(3), "writer"),
                Privilege(Id(4), "editor")) foreach Privilege.create
        }

        if (Users.findAll.isEmpty) {
            Seq(
                Users(Id(1), "admin@playjournal.com", "admin", "Admin", Privilege.admin),
                Users(Id(2), "user@playjournal.com", "user", "User", Privilege.standard),
                Users(Id(3), "writer@playjournal.com", "writer", "Writer", Privilege.writer),
                Users(Id(4), "editor@playjournal.com", "editor", "Editor", Privilege.editor)) foreach Users.create
        }
    }
}