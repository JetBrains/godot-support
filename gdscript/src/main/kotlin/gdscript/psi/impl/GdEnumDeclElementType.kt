package gdscript.psi.impl

import com.intellij.credentialStore.createSecureRandom
import com.intellij.psi.stubs.*
import com.jetbrains.rd.util.first
import com.jetbrains.rd.util.firstOrNull
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdEnumDeclStub
import gdscript.index.stub.GdEnumDeclStubImpl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.utils.PsiGdEnumUtil

object GdEnumDeclElementType : IStubElementType<GdEnumDeclStub, GdEnumDeclTl>("enumDecl", GdLanguage.INSTANCE) {

    @JvmStatic
    fun getInstance(debugName: String): GdEnumDeclElementType {
        return GdEnumDeclElementType;
    }

    override fun getExternalId(): String = "GdScript.enumDecl";

    override fun serialize(stub: GdEnumDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
        dataStream.writeName(stub.values().toString());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdEnumDeclStub =
        GdEnumDeclStubImpl(parentStub,
            dataStream.readNameString(),
            PsiGdEnumUtil.fromString(dataStream.readNameString())
        );

    override fun indexStub(stub: GdEnumDeclStub, sink: IndexSink) {
        var name: String? = stub.name();
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
        return GdEnumDeclStubImpl(parentStub, psi.enumDeclNmi?.name, psi.values);
    }

}
