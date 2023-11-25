package gdscript.parser

interface GdBaseParser {

    fun parse(b: GdPsiBuilder, optional: Boolean = false): Boolean

}
