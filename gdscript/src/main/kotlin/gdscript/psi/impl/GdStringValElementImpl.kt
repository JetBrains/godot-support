package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdStringValStub

abstract class GdStringValElementImpl : StubBasedPsiElementBase<GdStringValStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdStringValStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdStringVal"

}
