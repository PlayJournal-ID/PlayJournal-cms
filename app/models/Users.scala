package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import org.apache.commons.codec.digest.DigestUtils._
import play.api.Play.current

case class Users(email: String, password: String, name: String, privilege: Long, id: Long = 0)

object Users {
    val simple = {
        get[String]("users.email") ~
            get[String]("users.password") ~
            get[String]("users.name") ~
            get[Long]("users.privilege") ~
            get[Long]("users.id") map {
                case email ~ password ~ name ~ privilege ~ id => Users(email, password, name, privilege, id)
            }
    }

    private def passwordHash(pass: String, salt: String): String = sha256Hex(salt.padTo('0', 256) + pass)

    def authenticate(email: String, password: String): Option[Users] = {
        findByEmail(email).filter { user => user.password == passwordHash(password, email) }
    }

    def findByEmail(email: String): Option[Users] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM users WHERE users.email = {email}")
                .on('email -> email)
                .as(simple.singleOpt)
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
                .execute()
        }
    }
}