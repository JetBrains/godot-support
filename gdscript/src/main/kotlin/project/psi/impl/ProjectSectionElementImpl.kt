package project.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import project.index.stub.ProjectSectionStub

abstract class ProjectSectionElementImpl : StubBasedPsiElementBase<ProjectSectionStub> {

    constructor(node: ASTNode) : super(node)
    constructor(stub: ProjectSectionStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString() = "ProjectSection"

}
