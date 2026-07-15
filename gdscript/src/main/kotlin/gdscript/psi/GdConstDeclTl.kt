package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdConstDeclStub
import gdscript.psi.types.GdDocumented

interface GdConstDeclTl : GdTopLevelDecl, StubBasedPsiElement<GdConstDeclStub>, GdDocumented {
    val assignTyped: GdAssignTyped?

    val endStmt: GdEndStmt?

    val expr: GdExpr?

    val typed: GdTyped?

    val varNmi: GdVarNmi?

    fun getName(): String

    val returnType: String

    fun getPresentation(): ItemPresentation
}
