package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdClassVarDeclStub
import gdscript.psi.types.GdDocumented

interface GdClassVarDeclTl : GdTopLevelDecl, StubBasedPsiElement<GdClassVarDeclStub>, GdDocumented {
    val assignTyped: GdAssignTyped?

    val endStmt: GdEndStmt?

    val expr: GdExpr?

    val setgetDecl: GdSetgetDecl?

    val typed: GdTyped?

    val varNmi: GdVarNmi?

    fun getName(): String

    val isStatic: Boolean

    val returnType: String

    fun getPresentation(): ItemPresentation

    fun isAnnotated(annotator: String): Boolean
}
