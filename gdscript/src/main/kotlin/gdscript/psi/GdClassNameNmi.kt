package gdscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.StubBasedPsiElement
import gdscript.index.stub.GdClassIdStub

interface GdClassNameNmi : GdNamedIdElement, StubBasedPsiElement<GdClassIdStub> {
    val classId: String

    val parentName: String?

    val isInner: Boolean

    override fun setName(newName: String): PsiElement

    override fun getName(): String

    override fun getNameIdentifier(): PsiElement
}
