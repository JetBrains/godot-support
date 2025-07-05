package config.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import config.index.stub.ConfigAnnotationStub

abstract class GdConfigAnnotationElementImpl : StubBasedPsiElementBase<ConfigAnnotationStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: ConfigAnnotationStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "ConfigAnnotation"

}
