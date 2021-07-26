package gdscript.psi.impl

import com.intellij.psi.stubs.*
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdClassNamingStub
import gdscript.index.stub.GdClassNamingStubImpl
import gdscript.psi.GdClassNaming


object GdClassNamingElementType : IStubElementType<GdClassNamingStub, GdClassNaming>("classNaming", GdLanguage.INSTANCE) {

    fun getClassname(element: GdClassNamingImpl?): String {
        return element?.classNameNm?.name.toString();
    }

    @JvmStatic
    fun getInstance(debugName: String): GdClassNamingElementType {
        return GdClassNamingElementType
    }

    override fun getExternalId(): String = "GdScript.classNaming"

    override fun serialize(stub: GdClassNamingStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdClassNamingStub =
        GdClassNamingStubImpl(parentStub, dataStream.readName()?.string);

    override fun indexStub(stub: GdClassNamingStub, sink: IndexSink) {
        sink.occurrence(Indices.CLASS_NAMING_INDEX, stub.name())
    }

    override fun createPsi(stub: GdClassNamingStub): GdClassNaming =
            GdClassNamingImpl(stub, stub.stubType)

    override fun createStub(psi: GdClassNaming, parentStub: StubElement<*>?): GdClassNamingStub {
        return GdClassNamingStubImpl(parentStub, psi.classNameNm?.name)
    }

}
