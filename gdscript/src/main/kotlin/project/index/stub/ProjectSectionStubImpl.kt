package project.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import project.psi.ProjectSection
import project.psi.impl.ProjectSectionElementType

class ProjectSectionStubImpl : StubBase<ProjectSection>, ProjectSectionStub {

    private var name: String = ""

    constructor(parent: StubElement<*>?, name: String) : super(parent, ProjectSectionElementType) {
        this.name = name
    }

    override fun getName(): String {
        return name
    }

}
