package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdMethodDeclStub
import gdscript.psi.types.GdDocumented

interface GdMethodDeclTl : GdTopLevelDecl, StubBasedPsiElement<GdMethodDeclStub>, GdDocumented {
    val methodIdNmi: GdMethodIdNmi?

    val methodSpecifierList: List<GdMethodSpecifier>

    val paramList: GdParamList?

    val returnHint: GdReturnHint?

    val stmtOrSuite: GdStmtOrSuite?

    val isStatic: Boolean

    val isVariadic: Boolean

    fun getName(): String

    val returnType: String

    val parameters: LinkedHashMap<String, String>

    fun getPresentation(): ItemPresentation

    val isConstructor: Boolean

}

