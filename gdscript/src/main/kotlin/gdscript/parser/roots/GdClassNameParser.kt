package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mceEndStmt
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdClassNameParser : GdBaseParser() {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(CLASS_NAME)) return optional

        val m = b.mark()
        var ok = b.consumeToken(CLASS_NAME)
        ok = ok && b.mceIdentifier(CLASS_NAME_NMI)
        ok = ok && b.mceEndStmt()

        if (ok) m.done(CLASS_NAMING)
        else m.error("Expected class_name")

        return ok || optional
    }

}
