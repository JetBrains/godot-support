package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdMethodDeclStub

abstract class GdMethodDeclElementImpl : StubBasedPsiElementBase<GdMethodDeclStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdMethodDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdMethodDecl"

}
