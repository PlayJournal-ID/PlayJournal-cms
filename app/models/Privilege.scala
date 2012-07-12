package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import play.api.Play.current

case class Privilege(id: Pk[Long], description: String)

object Privilege {
    val simple = {
        get[Pk[Long]]("privilge.id") ~
        get[String]("privilege.description") map {
            case id~description => Privilege(id, description)
        }
    }
    
    // Accessor for each privilege. With this we can access them by calling Privilege.admin (or other)
    def admin(): Long = getId("admin")
    def standard(): Long = getId("standard")
    def writer(): Long = getId("writer")
    def editor(): Long = getId("editor")
    
    def getId(desc: String): Long = {
        DB.withConnection{ implicit connection =>
            SQL("SELECT p.id FROM privilege p WHERE p.description = {description}")
            	.on('description -> desc)
            	.as(scalar[Long].single)
        }
    }
    
    def findAll(): Seq[Privilege] = {
        DB.withConnection{ implicit connection =>
            SQL("SELECT * FROM privilege").as(simple *)
        }
    }
    
    def create(privilege: Privilege) = {
        DB.withConnection{ implicit connection =>
            SQL("INSERT INTO privilege (description) VALUES ({description})")
            	.on('description -> privilege.description)
            	.executeInsert()
        }
    }
}
