package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdClassDeclStub

abstract class GdClassDeclElementImpl : StubBasedPsiElementBase<GdClassDeclStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdClassDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdClassDecl"

}
