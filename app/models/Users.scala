package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import org.apache.commons.codec.digest.DigestUtils._
import play.api.Play.current

case class Users(id: Pk[Long], email: String, password: String, name: String, privilege: Long = Privilege.standard)

object Users {
    val simple = {
        get[Pk[Long]]("users.id") ~
            get[String]("users.email") ~
            get[String]("users.password") ~
            get[String]("users.name") ~
            get[Long]("users.privilege") map {
                case id ~ email ~ password ~ name ~ privilege => Users(id, email, password, name, privilege)
            }
    }

    private def passwordHash(pass: String, salt: String): String = sha256Hex(salt.padTo('0', 256) + pass)

    def authenticate(email: String, password: String): Option[Users] = {
        findByEmail(email).filter { user => user.password == passwordHash(password, email) }
    }

    def findById(id: Long): Option[Users] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM users WHERE users.id = {id}")
                .on('id -> id)
                .as(simple.singleOpt)
        }
    }
    
    def findByEmail(email: String): Option[Users] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM users WHERE users.email = {email}")
                .on('email -> email)
                .as(simple.singleOpt)
        }
    }
    
    def findAll(): Seq[Users] = {
        DB.withConnection{ implicit connection =>
            SQL("SELECT * FROM users").as(simple *)
        }
    }

    def create(user: Users) {
        DB.withConnection { implicit connection =>
            SQL("INSERT INTO users (email, password, name, privilege) VALUES ({email}, {password}, {name}, {privilege})")
                .on(
                    'email -> user.email,
                    'password -> passwordHash(user.password, user.email),
                    'name -> user.name,
                    'privilege -> user.privilege)
                .executeInsert()
        }
    }
}