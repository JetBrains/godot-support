package gdscript.psi.impl

import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdInheritanceStub
import gdscript.index.stub.GdInheritanceStubImpl
import gdscript.psi.GdInheritance


object GdInheritanceElementType : IStubElementType<GdInheritanceStub, GdInheritance>("inheritance", GdLanguage.INSTANCE) {

    @JvmStatic
    fun getInstance(debugName: String): GdInheritanceElementType {
        return GdInheritanceElementType
    }

    override fun getExternalId(): String = "GdScript.inheritance"

    override fun serialize(stub: GdInheritanceStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdInheritanceStub =
        GdInheritanceStubImpl(parentStub, dataStream.readName()?.string);

    override fun indexStub(stub: GdInheritanceStub, sink: IndexSink) {
        sink.occurrence(Indices.INHERITANCE_INDEX, stub.name());
    }

    override fun createPsi(stub: GdInheritanceStub): GdInheritance =
        GdInheritanceImpl(stub, stub.stubType);

    override fun createStub(psi: GdInheritance, parentStub: StubElement<*>?): GdInheritanceStub {
        return GdInheritanceStubImpl(parentStub, psi.inheritanceIdNmi?.name);
    }

}
