package gdscript.psi

import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdSignalDeclStub
import gdscript.psi.types.GdDocumented

interface GdSignalDeclTl : GdTopLevelDecl, StubBasedPsiElement<GdSignalDeclStub>, GdDocumented {
    val endStmt: GdEndStmt?

    val paramList: GdParamList?

    val signalIdNmi: GdSignalIdNmi?

    fun getName(): String

    val parameters: LinkedHashMap<String, String>
}
