package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.util.IncorrectOperationException
import tscn.index.stub.TscnNodeHeaderStub

abstract class TscnNodeHeaderElementImpl : StubBasedPsiElementBase<TscnNodeHeaderStub>, PsiNamedElement {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnNodeHeaderStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnNodeHeader"

    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException("Renaming a scene node is not supported yet")
    }

}
