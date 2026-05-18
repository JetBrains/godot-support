package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import tscn.index.stub.TscnGdResourceHeaderStub

abstract class TscnGdResourceHeaderElementImpl : StubBasedPsiElementBase<TscnGdResourceHeaderStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnGdResourceHeaderStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnGdResourceHeader"

}
