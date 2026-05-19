package tscn.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import tscn.index.stub.TscnSceneHeaderStub

abstract class TscnSceneHeaderElementImpl : StubBasedPsiElementBase<TscnSceneHeaderStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: TscnSceneHeaderStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "TscnSceneHeader"

}
