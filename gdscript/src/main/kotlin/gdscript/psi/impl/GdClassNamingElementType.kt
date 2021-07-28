package gdscript.psi.impl

import com.intellij.psi.stubs.*
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdClassNamingStub
import gdscript.index.stub.GdClassNamingStubImpl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance


object GdClassNamingElementType : IStubElementType<GdClassNamingStub, GdClassNaming>("classNaming", GdLanguage.INSTANCE) {

    fun getClassname(element: GdClassNamingImpl?): String {
        return element?.classNameNm?.name.toString();
    }

    fun getParentName(element: GdClassNamingImpl?): String? {
        val inh = PsiTreeUtil.getPrevSiblingOfType(element, GdInheritance::class.java)

        return inh?.inheritanceName
    }

    @JvmStatic
    fun getInstance(debugName: String): GdClassNamingElementType {
        return GdClassNamingElementType
    }

    override fun getExternalId(): String = "GdScript.classNaming"

    override fun serialize(stub: GdClassNamingStub, dataStream: StubOutputStream) {
        dataStream.writeUTF(stub.parent());
        dataStream.writeName(stub.name());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdClassNamingStub {
        val parentName = dataStream.readUTF();

        return GdClassNamingStubImpl(parentStub, dataStream.readName()?.string, parentName);
    }

    override fun indexStub(stub: GdClassNamingStub, sink: IndexSink) {
        sink.occurrence(Indices.CLASS_NAMING_INDEX, stub.name())
    }

    override fun createPsi(stub: GdClassNamingStub): GdClassNaming =
            GdClassNamingImpl(stub, stub.stubType)

    override fun createStub(psi: GdClassNaming, parentStub: StubElement<*>?): GdClassNamingStub {
        val inheritance = psi.prevSibling as GdInheritance?;

        return GdClassNamingStubImpl(parentStub, psi.classNameNm?.name, inheritance?.inheritanceName)
    }

}
