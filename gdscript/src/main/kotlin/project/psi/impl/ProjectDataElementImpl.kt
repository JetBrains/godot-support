package project.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import project.index.stub.ProjectDataStub

abstract class ProjectDataElementImpl : StubBasedPsiElementBase<ProjectDataStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: ProjectDataStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "ProjectData"

}
