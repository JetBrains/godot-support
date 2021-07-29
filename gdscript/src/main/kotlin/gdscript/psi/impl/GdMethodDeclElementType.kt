package gdscript.psi.impl

import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdMethodDeclStub
import gdscript.index.stub.GdMethodDeclStubImpl
import gdscript.psi.GdMethodDeclTl

object GdMethodDeclElementType : IStubElementType<GdMethodDeclStub, GdMethodDeclTl>("methodDecl", GdLanguage.INSTANCE) {

    @JvmStatic
    fun getInstance(debugName: String): GdMethodDeclElementType {
        return GdMethodDeclElementType;
    }

    override fun getExternalId(): String = "GdScript.methodDecl";

    override fun serialize(stub: GdMethodDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdMethodDeclStub =
        GdMethodDeclStubImpl(parentStub, dataStream.readName()?.string);

    override fun indexStub(stub: GdMethodDeclStub, sink: IndexSink) {
        sink.occurrence(Indices.METHOD_DECL, stub.name());
    }

    override fun createPsi(stub: GdMethodDeclStub): GdMethodDeclTl =
        GdMethodDeclTlImpl(stub, stub.stubType);

    override fun createStub(psi: GdMethodDeclTl, parentStub: StubElement<*>?): GdMethodDeclStub {
        return GdMethodDeclStubImpl(parentStub, psi.methodName);
    }

}
