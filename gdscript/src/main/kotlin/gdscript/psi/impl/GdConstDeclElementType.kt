package gdscript.psi.impl

import com.intellij.psi.stubs.*
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdConstDeclStub
import gdscript.index.stub.GdConstDeclStubImpl
import gdscript.model.GdCommentModel
import gdscript.psi.GdConstDeclTl
import gdscript.psi.utils.GdCommentUtil

object GdConstDeclElementType : IStubElementType<GdConstDeclStub, GdConstDeclTl>("constDecl", GdLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdConstDeclElementType {
        return GdConstDeclElementType
    }

    override fun getExternalId(): String = "GdScript.constDecl"

    override fun serialize(stub: GdConstDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name())
        GdCommentModel.serializeDocumentation(stub, dataStream)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdConstDeclStub =
        GdConstDeclStubImpl(
            parentStub, dataStream.readNameString(),
            GdCommentModel(dataStream),
        )

    override fun indexStub(stub: GdConstDeclStub, sink: IndexSink) {
        sink.occurrence(Indices.CONST_DECL, stub.name())
    }

    override fun createPsi(stub: GdConstDeclStub): GdConstDeclTl =
        GdConstDeclTlImpl(stub, stub.stubType)

    override fun createStub(psi: GdConstDeclTl, parentStub: StubElement<*>?): GdConstDeclStub {
        return GdConstDeclStubImpl(
            parentStub, psi.name,
            GdCommentUtil.collectComments(psi),
        )
    }

}
