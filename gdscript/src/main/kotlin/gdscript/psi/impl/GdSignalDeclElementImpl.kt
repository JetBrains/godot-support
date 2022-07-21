package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdSignalDeclStub

abstract class GdSignalDeclElementImpl : StubBasedPsiElementBase<GdSignalDeclStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdSignalDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdSignalDecl"

}
