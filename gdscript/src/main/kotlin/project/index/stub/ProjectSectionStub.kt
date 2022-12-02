package project.index.stub

import com.intellij.psi.stubs.StubElement
import project.psi.ProjectSection

interface ProjectSectionStub : StubElement<ProjectSection> {

    fun getName(): String;

}
