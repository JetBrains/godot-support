package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdClassNamingStub

abstract class GdClassNamingElementImpl : StubBasedPsiElementBase<GdClassNamingStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdClassNamingStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdClassNaming"

}
