package gdscript.psi

import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdClassDeclStub
import gdscript.psi.types.GdDocumented

interface GdClassDeclTl : GdTopLevelDecl, StubBasedPsiElement<GdClassDeclStub>, GdDocumented {
    val classNameNmi: GdClassNameNmi?

    val inheritanceList: List<GdInheritance>

    val topLevelDeclList: List<GdTopLevelDecl>

    fun getName(): String

    val parentName: String
}
