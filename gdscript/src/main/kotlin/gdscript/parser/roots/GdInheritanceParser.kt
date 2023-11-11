package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mcAnyOf
import gdscript.utils.PsiBuilderUtil.mcToken
import gdscript.utils.PsiBuilderUtil.mceEndStmt
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdInheritanceParser : GdBaseParser() {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(EXTENDS)) return optional

        val m = b.mark()
        var ok = b.consumeToken(EXTENDS)
        ok = ok && b.mcAnyOf(INHERITANCE_ID_NM, STRING, IDENTIFIER)
        while (ok && b.consumeToken(DOT)) {
            ok = b.mcToken(INHERITANCE_SUB_ID_NM, IDENTIFIER)
        }
        ok = ok && b.mceEndStmt()

        if (ok) m.done(INHERITANCE)
        else m.error("Expected INHERITANCE")

        return ok || optional
    }

}
