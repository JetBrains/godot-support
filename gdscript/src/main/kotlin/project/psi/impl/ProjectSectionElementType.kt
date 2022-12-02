package project.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import project.ProjectLanguage
import project.index.ProjectIndices
import project.index.stub.ProjectSectionStub
import project.index.stub.ProjectSectionStubImpl
import project.psi.ProjectSection

object ProjectSectionElementType : IStubElementType<ProjectSectionStub, ProjectSection>("section", ProjectLanguage) {

    @JvmStatic
    fun getInstance(debugName: String): ProjectSectionElementType = ProjectSectionElementType

    override fun getExternalId(): String = "project.section"

    override fun serialize(stub: ProjectSectionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getName());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): ProjectSectionStub =
        ProjectSectionStubImpl(
            parentStub,
            dataStream.readNameString() ?: "",
        )

    override fun indexStub(stub: ProjectSectionStub, sink: IndexSink) {
        sink.occurrence(ProjectIndices.SECTION_INDEX, stub.getName());
    }

    override fun createPsi(stub: ProjectSectionStub): ProjectSection =
        ProjectSectionImpl(stub, stub.stubType);

    override fun createStub(psi: ProjectSection, parentStub: StubElement<out PsiElement>?): ProjectSectionStub =
        ProjectSectionStubImpl(parentStub, psi.name);

}
