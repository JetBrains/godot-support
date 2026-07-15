package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdEnumDeclStub
import gdscript.psi.types.GdDocumented

interface GdEnumDeclTl : GdTopLevelDecl, StubBasedPsiElement<GdEnumDeclStub>, GdDocumented {
    val endStmt: GdEndStmt?

    val enumDeclNmi: GdEnumDeclNmi?

    val enumValueList: List<GdEnumValue>

    fun getName(): String

    val values: LinkedHashMap<String, Long>

    fun getPresentation(): ItemPresentation
}
