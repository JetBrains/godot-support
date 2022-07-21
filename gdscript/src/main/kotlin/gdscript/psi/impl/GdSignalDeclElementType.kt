package gdscript.psi.impl

import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdSignalDeclStub
import gdscript.index.stub.GdSignalDeclStubImpl
import gdscript.psi.GdSignalDeclTl

object GdSignalDeclElementType : IStubElementType<GdSignalDeclStub, GdSignalDeclTl>("signalDecl", GdLanguage.INSTANCE) {

    @JvmStatic
    fun getInstance(debugName: String): GdSignalDeclElementType {
        return GdSignalDeclElementType;
    }

    override fun getExternalId(): String = "GdScript.signalDecl";

    override fun serialize(stub: GdSignalDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
        dataStream.writeName("TODO"); // TODO
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdSignalDeclStub =
        GdSignalDeclStubImpl(parentStub, dataStream.readNameString(), arrayOf("TODO"));

    override fun indexStub(stub: GdSignalDeclStub, sink: IndexSink) {
        sink.occurrence(Indices.SIGNAL_DECL, stub.name());
    }

    override fun createPsi(stub: GdSignalDeclStub): GdSignalDeclTl =
        GdSignalDeclTlImpl(stub, stub.stubType);

    override fun createStub(psi: GdSignalDeclTl, parentStub: StubElement<*>?): GdSignalDeclStub {
        return GdSignalDeclStubImpl(parentStub, psi.name, psi.parameters);
    }

}
