package gdscript.psi.impl

import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdClassVarDeclStub
import gdscript.index.stub.GdClassVarDeclStubImpl
import gdscript.psi.GdClassVarDeclTl

object GdClassVarDeclElementType : IStubElementType<GdClassVarDeclStub, GdClassVarDeclTl>("classVarDecl", GdLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdClassVarDeclElementType {
        return GdClassVarDeclElementType;
    }

    override fun getExternalId(): String = "GdScript.classVarDecl";

    override fun serialize(stub: GdClassVarDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdClassVarDeclStub =
        GdClassVarDeclStubImpl(parentStub, dataStream.readNameString());

    override fun indexStub(stub: GdClassVarDeclStub, sink: IndexSink) {
        sink.occurrence(Indices.CLASS_VAR, stub.name());
    }

    override fun createPsi(stub: GdClassVarDeclStub): GdClassVarDeclTl =
        GdClassVarDeclTlImpl(stub, stub.stubType);

    override fun createStub(psi: GdClassVarDeclTl, parentStub: StubElement<*>?): GdClassVarDeclStub {
        return GdClassVarDeclStubImpl(parentStub, psi.name);
    }

}
