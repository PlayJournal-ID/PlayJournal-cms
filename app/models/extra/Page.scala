package models.extra

case class Page[A](items: Seq[A], page: Long, offset: Long, total: Long) {
    lazy val prev = Option(page - 1).filter(_ > 0)
    lazy val next = Option(page + 1).filter(_ => (page * offset) < total)
}