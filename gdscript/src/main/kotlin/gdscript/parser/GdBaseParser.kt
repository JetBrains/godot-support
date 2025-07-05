package gdscript.parser

interface GdBaseParser {

    fun parse(b: GdPsiBuilder, l: Int, optional: Boolean = false): Boolean

}
