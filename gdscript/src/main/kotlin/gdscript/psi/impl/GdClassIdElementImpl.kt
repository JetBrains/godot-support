package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdClassIdStub

abstract class GdClassIdElementImpl : StubBasedPsiElementBase<GdClassIdStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdClassIdStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdClasId"

}
