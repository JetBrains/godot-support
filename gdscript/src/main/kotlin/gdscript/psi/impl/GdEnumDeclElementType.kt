package gdscript.psi.impl

import com.intellij.credentialStore.createSecureRandom
import com.intellij.psi.stubs.*
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdEnumDeclStub
import gdscript.index.stub.GdEnumDeclStubImpl
import gdscript.model.GdCommentModel
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.utils.GdCommentUtil
import gdscript.psi.utils.PsiGdEnumUtil

object GdEnumDeclElementType : IStubElementType<GdEnumDeclStub, GdEnumDeclTl>("enumDecl", GdLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdEnumDeclElementType {
        return GdEnumDeclElementType;
    }

    override fun getExternalId(): String = "GdScript.enumDecl";

    override fun serialize(stub: GdEnumDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name())
        dataStream.writeName(stub.values().toString())
        GdCommentModel.serializeDocumentation(stub, dataStream)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdEnumDeclStub =
        GdEnumDeclStubImpl(
            parentStub,
            dataStream.readNameString(),
            PsiGdEnumUtil.fromString(dataStream.readNameString()),
            GdCommentModel(dataStream),
        );

    override fun indexStub(stub: GdEnumDeclStub, sink: IndexSink) {
        var name: String? = stub.name();
        // TODO losos
        if (name == null || name.isEmpty()) {
            name = stub.values().keys.firstOrNull();
        }
        if (name == null || name.isEmpty()) {
            name = createSecureRandom().toString();
        }

        sink.occurrence(Indices.ENUM, name);
    }

    override fun createPsi(stub: GdEnumDeclStub): GdEnumDeclTl =
        GdEnumDeclTlImpl(stub, stub.stubType);

    override fun createStub(psi: GdEnumDeclTl, parentStub: StubElement<*>?): GdEnumDeclStub {
        return GdEnumDeclStubImpl(
            parentStub, psi.enumDeclNmi?.name, psi.values,
            GdCommentUtil.collectComments(psi),
        )
    }

}
