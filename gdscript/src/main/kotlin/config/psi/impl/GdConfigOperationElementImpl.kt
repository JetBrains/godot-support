package config.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import config.index.stub.ConfigOperationStub

abstract class GdConfigOperationElementImpl : StubBasedPsiElementBase<ConfigOperationStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: ConfigOperationStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "ConfigOperation"

}
