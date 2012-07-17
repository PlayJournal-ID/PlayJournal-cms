package views.helper

import java.text.SimpleDateFormat
import java.util.Date

import org.clapper.markwrap._

object Format {
    def date(date: Date, format: String = "dd MMMM yyyy HH:mm") = {
        new SimpleDateFormat(format).format(date)
    }

    def markdown(content: String): String = {
        val parser = MarkWrap.parserFor(MarkupType.Markdown)
        parser.parseToHTML(content)
    }
}