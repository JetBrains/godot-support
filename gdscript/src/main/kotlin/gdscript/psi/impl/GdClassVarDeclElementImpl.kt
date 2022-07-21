package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdClassVarDeclStub

abstract class GdClassVarDeclElementImpl : StubBasedPsiElementBase<GdClassVarDeclStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdClassVarDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdClassVarDecl"

}
