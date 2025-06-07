package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import tscn.index.stub.TscnNodeHeaderStub

abstract class TscnNodeHeaderElementImpl : StubBasedPsiElementBase<TscnNodeHeaderStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnNodeHeaderStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnNodeHeader"

}
