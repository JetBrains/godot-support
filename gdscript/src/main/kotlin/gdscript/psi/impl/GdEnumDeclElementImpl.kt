package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdEnumDeclStub

abstract class GdEnumDeclElementImpl : StubBasedPsiElementBase<GdEnumDeclStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdEnumDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdEnumDecl"

}
