package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import java.util.Date
import play.api.Play.current

import models.extra.Page

case class Post(id: Pk[Long], title: String, content: String, created: Date = new Date(), lastUpdate: Date = new Date(), writer: Long = 0) {
    val titleSlug: String = title.toLowerCase.replace(" ", "-")
}

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

    def getPageItem(page: Long, pageSize: Long = 10) = {
        Page(findFrontPage(page, pageSize), page, pageSize, getTotalRows())
    }

    def getPostByWriterPaginated(writer: Long, page: Long, pageSize: Long = 10) = {
        Page(findByWriterPaginated(writer, page, pageSize), page, pageSize, getTotalRowsByWriter(writer))
    }

    def getTotalRows(): Long = {
        DB.withConnection { implicit connection =>
            SQL("SELECT count(post.id) FROM post").as(scalar[Long].single)
        }
    }

    def getTotalRowsByWriter(writer: Long): Long = {
        DB.withConnection { implicit connection =>
            SQL("SELECT count(post.id) FROM post WHERE post.writer = {writer}")
                .on('writer -> writer)
                .as(scalar[Long].single)
        }
    }

    def findFrontPage(page: Long, postPerPage: Long = 10): Seq[Post] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM post ORDER BY post.last_update DESC LIMIT {limit} OFFSET {offset}")
                .on(
                    'limit -> postPerPage,
                    'offset -> (page - 1) * postPerPage
                )
                .as(simple *)
        }
    }

    def findById(id: Long): Option[Post] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM post WHERE post.id = {id}")
                .on('id -> id)
                .as(simple.singleOpt)
        }
    }

    def findByWriterPaginated(writer: Long, page: Long, postPerPage: Long = 10) = {
        DB.withConnection { implicit connection =>
            SQL("""SELECT * FROM post WHERE post.writer = {writer}
                       ORDER BY post.last_update DESC
                       LIMIT {limit} OFFSET {offset}
                """)
                .on(
                    'writer -> writer,
                    'limit -> postPerPage,
                    'offset -> (page - 1) * postPerPage
                )
                .as(simple *)
        }
    }

    def findByWriter(writer: Long): Seq[Post] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM post WHERE post.writer = {writer}")
                .on('writer -> writer)
                .as(simple *)
        }
    }

    def findAll(): Seq[Post] = {
        DB.withConnection { implicit connection =>
            SQL("SELECT * FROM Post").as(simple *)
        }
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

    def update(id: Long, title: String, content: String) = {
        DB.withConnection { implicit connection =>
            SQL("""
                    UPDATE post SET title = {title}, content = {content}
                    WHERE id = {id}
                """)
                .on(
                    'title -> title,
                    'content -> content,
                    'id -> id
                )
                .executeUpdate()
        }
    }
}