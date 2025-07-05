package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdConstDeclStub

abstract class GdConstDeclElementImpl : StubBasedPsiElementBase<GdConstDeclStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdConstDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdConstDecl"

}
