package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import tscn.index.stub.TscnExtHeaderStub

abstract class TscnExtHeaderElementImpl : StubBasedPsiElementBase<TscnExtHeaderStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnExtHeaderStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnExtHeader"

}
