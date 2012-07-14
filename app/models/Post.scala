package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import java.util.Date
import play.api.Play.current

import org.clapper.markwrap._

case class Post(id: Pk[Long], title: String, content: String, created: Date = new Date(), lastUpdate: Date = new Date(), writer: Long = 0)

object Post {
    val simple = {
        get[Pk[Long]]("post.id") ~
            get[String]("post.title") ~
            get[String]("post.content") ~
            get[Date]("post.created") ~
            get[Date]("post.last_update") ~
            get[Long]("post.writer") map {
                case id ~ title ~ content ~ created ~ lastUpdate ~ writer => Post(id, title, content, created, lastUpdate, writer)
            }
    }
    
    def findById(id: Long) = {
        DB.withConnection { implicit connection => 
            SQL("SELECT * FROM post WHERE post.id = {id}")
            	.on('id -> id)
            	.as(simple.singleOpt)
        }
    }
    
    def findByWriter(writer: Long): Seq[Post] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM post WHERE post.writer = {writer}")
            	.on('writer -> writer)
            	.as(simple *)
        }
    }
    
    def contentToHTML(content: String) = {
        val parser = MarkWrap.parserFor(MarkupType.Markdown)
        parser.parseToHTML(content)
    }
    
    def create(title: String, content: String, userId: Long) = {
        DB.withConnection { implicit connection =>
            SQL("""
                   INSERT INTO post (title, content, created, writer) 
                   VALUES ({title}, {content}, NOW(), {writer})
                """)
                   .on(
                       'title -> title,
                       'content -> content,
                       'writer -> userId
                   )
                   .executeInsert()
        }
    }
}