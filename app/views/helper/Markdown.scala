package views.helper

object Markdown {
    def getFirstParagraph(txt: String) = {
        val newLineIdx = txt.indexOf("\n") match {
            case i: Int if i > 0  => i
            case i: Int if i <= 0 => txt.length
        }

        txt.substring(0, newLineIdx)
    }
}