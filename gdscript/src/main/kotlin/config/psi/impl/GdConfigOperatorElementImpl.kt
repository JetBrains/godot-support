package config.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import config.index.stub.ConfigOperatorStub

abstract class GdConfigOperatorElementImpl : StubBasedPsiElementBase<ConfigOperatorStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: ConfigOperatorStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "ConfigOperator"

}
