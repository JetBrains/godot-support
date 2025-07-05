package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import tscn.index.stub.TscnParagraphStub

abstract class TscnParagraphElementImpl : StubBasedPsiElementBase<TscnParagraphStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnParagraphStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnParagarph"

}
