package project.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import project.ProjectLanguage
import project.index.ProjectIndices
import project.index.stub.ProjectDataStub
import project.index.stub.ProjectDataStubImpl
import project.psi.ProjectData

object ProjectDataElementType : IStubElementType<ProjectDataStub, ProjectData>("data", ProjectLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): ProjectDataElementType = ProjectDataElementType

    override fun getExternalId(): String = "project.data"

    override fun serialize(stub: ProjectDataStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getKey());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): ProjectDataStub =
        ProjectDataStubImpl(
            parentStub,
            dataStream.readNameString() ?: "",
        )

    override fun indexStub(stub: ProjectDataStub, sink: IndexSink) {
        sink.occurrence(ProjectIndices.DATA_INDEX, stub.getKey());
    }

    override fun createPsi(stub: ProjectDataStub): ProjectData =
        ProjectDataImpl(stub, stub.stubType);

    override fun createStub(psi: ProjectData, parentStub: StubElement<out PsiElement>?): ProjectDataStub =
        ProjectDataStubImpl(parentStub, psi.key);

}
