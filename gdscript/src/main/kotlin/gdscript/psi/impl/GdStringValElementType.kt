package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdStringValStub
import gdscript.index.stub.GdStringValStubImpl
import gdscript.psi.GdStringVal

object GdStringValElementType : IStubElementType<GdStringValStub, GdStringVal>("stringVal", GdLanguage) {

    @JvmStatic
    fun getInstance(debugName: String): GdStringValElementType {
        return GdStringValElementType;
    }

    override fun getExternalId(): String = "GdScript.stringVal";

    override fun serialize(stub: GdStringValStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdStringValStub {
        return GdStringValStubImpl(
            parentStub,
            dataStream.readNameString(),
        );
    }

    override fun indexStub(stub: GdStringValStub, sink: IndexSink) {
        sink.occurrence(Indices.USER_FILES, stub.name());
    }

    override fun createPsi(stub: GdStringValStub): GdStringVal =
        GdStringValImpl(stub, stub.stubType);

    override fun createStub(psi: GdStringVal, parentStub: StubElement<*>?): GdStringValStub {
        return GdStringValStubImpl(parentStub, psi.text);
    }

    override fun shouldCreateStub(node: ASTNode?): Boolean {
        return node?.psi?.text?.trim('"')?.startsWith("user://") ?: false
    }

}
