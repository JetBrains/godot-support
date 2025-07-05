package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import tscn.index.stub.TscnResourceHeaderStub

abstract class TscnResourceHeaderElementImpl : StubBasedPsiElementBase<TscnResourceHeaderStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnResourceHeaderStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnResourceHeader"

}
