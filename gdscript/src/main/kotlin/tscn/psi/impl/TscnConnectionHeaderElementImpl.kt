package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import tscn.index.stub.TscnConnectionHeaderStub

abstract class TscnConnectionHeaderElementImpl : StubBasedPsiElementBase<TscnConnectionHeaderStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnConnectionHeaderStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnConnectionHeader"

}
