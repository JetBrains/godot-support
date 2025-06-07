package gdscript.psi.impl

import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdSignalDeclStub
import gdscript.index.stub.GdSignalDeclStubImpl
import gdscript.model.GdCommentModel
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.utils.GdCommentUtil
import gdscript.psi.utils.PsiGdParameterUtil

object GdSignalDeclElementType : IStubElementType<GdSignalDeclStub, GdSignalDeclTl>("signalDecl", GdLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdSignalDeclElementType {
        return GdSignalDeclElementType
    }

    override fun getExternalId(): String = "GdScript.signalDecl";

    override fun serialize(stub: GdSignalDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name())
        dataStream.writeName(stub.parameters().toString())
        GdCommentModel.serializeDocumentation(stub, dataStream)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdSignalDeclStub {
        return GdSignalDeclStubImpl(
            parentStub,
            dataStream.readNameString(),
            PsiGdParameterUtil.fromString(dataStream.readNameString()),
            GdCommentModel(dataStream),
        )
    }

    override fun indexStub(stub: GdSignalDeclStub, sink: IndexSink) {
        sink.occurrence(Indices.SIGNAL_DECL, stub.name())
    }

    override fun createPsi(stub: GdSignalDeclStub): GdSignalDeclTl =
        GdSignalDeclTlImpl(stub, stub.stubType)

    override fun createStub(psi: GdSignalDeclTl, parentStub: StubElement<*>?): GdSignalDeclStub {
        return GdSignalDeclStubImpl(parentStub, psi.name, psi.parameters, GdCommentUtil.collectComments(psi))
    }

}
