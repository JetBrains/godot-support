package gdscript.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdInheritanceStub

abstract class GdInheritanceElementImpl : StubBasedPsiElementBase<GdInheritanceStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: GdInheritanceStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "GdInheritance"

}
