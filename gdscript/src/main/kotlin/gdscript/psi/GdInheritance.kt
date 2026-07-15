package gdscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdInheritanceStub

interface GdInheritance : PsiElement, StubBasedPsiElement<GdInheritanceStub> {
    val endStmt: GdEndStmt?

    val inheritanceId: GdInheritanceId?

    val inheritancePath: String
}
