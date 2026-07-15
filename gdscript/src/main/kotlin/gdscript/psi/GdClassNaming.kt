package gdscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdClassNamingStub
import gdscript.psi.types.GdDocumented

interface GdClassNaming : PsiElement, StubBasedPsiElement<GdClassNamingStub>, GdDocumented {
    val classNameNmi: GdClassNameNmi?

    val endStmt: GdEndStmt?

    val classname: String

    val parentName: String
}
