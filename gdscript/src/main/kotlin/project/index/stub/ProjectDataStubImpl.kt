package project.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import project.psi.ProjectData
import project.psi.impl.ProjectDataElementType

class ProjectDataStubImpl : StubBase<ProjectData>, ProjectDataStub {

    private var key: String = "";

    constructor(parent: StubElement<*>?, key: String) : super(parent, ProjectDataElementType) {
        this.key = key;
    }

    override fun getKey(): String {
        return key;
    }

}
