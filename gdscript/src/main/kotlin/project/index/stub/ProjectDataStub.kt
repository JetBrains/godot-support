package project.index.stub

import com.intellij.psi.stubs.StubElement
import project.psi.ProjectData

interface ProjectDataStub : StubElement<ProjectData> {

    fun getKey(): String

}
