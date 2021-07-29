package gdscript.psi.impl

import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdConstDeclStub
import gdscript.index.stub.GdConstDeclStubImpl
import gdscript.psi.GdConstDeclTl

object GdConstDeclElementType : IStubElementType<GdConstDeclStub, GdConstDeclTl>("constDecl", GdLanguage.INSTANCE) {

    @JvmStatic
    fun getInstance(debugName: String): GdConstDeclElementType {
        return GdConstDeclElementType;
    }

    override fun getExternalId(): String = "GdScript.constDecl";

    override fun serialize(stub: GdConstDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
        dataStream.writeName(stub.returnType().orEmpty());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdConstDeclStub =
        GdConstDeclStubImpl(parentStub, dataStream.readNameString(), dataStream.readNameString());

    override fun indexStub(stub: GdConstDeclStub, sink: IndexSink) {
        sink.occurrence(Indices.CONST_DECL_INDEX, stub.name());
    }

    override fun createPsi(stub: GdConstDeclStub): GdConstDeclTl =
        GdConstDeclTlImpl(stub, stub.stubType);

    override fun createStub(psi: GdConstDeclTl, parentStub: StubElement<*>?): GdConstDeclStub {
        return GdConstDeclStubImpl(parentStub, psi.constName, psi.returnType);
    }

}
