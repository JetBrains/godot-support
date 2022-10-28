package gdscript.psi.impl

import com.intellij.psi.stubs.*
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdClassNamingStub
import gdscript.index.stub.GdClassNamingStubImpl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance

object GdClassNamingElementType : IStubElementType<GdClassNamingStub, GdClassNaming>("classNaming", GdLanguage) {

    fun getClassname(element: GdClassNaming?): String {
        return element?.classNameNmi?.name.toString();
    }

    /**
     * @return name of extended class
     */
    fun getParentName(element: GdClassNaming): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parent();
        }

        return PsiTreeUtil.getStubChildOfType(element.parent, GdInheritance::class.java)?.inheritancePath.orEmpty();
    }

    @JvmStatic
    fun getInstance(debugName: String): GdClassNamingElementType {
        return GdClassNamingElementType
    }

    override fun getExternalId(): String = "GdScript.classNaming"

    override fun serialize(stub: GdClassNamingStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
        dataStream.writeName(stub.parent());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdClassNamingStub {
        return GdClassNamingStubImpl(parentStub, dataStream.readNameString().orEmpty(), dataStream.readName()?.string);
    }

    override fun indexStub(stub: GdClassNamingStub, sink: IndexSink) {
        sink.occurrence(Indices.CLASS_NAMING, stub.name())
    }

    override fun createPsi(stub: GdClassNamingStub): GdClassNaming =
            GdClassNamingImpl(stub, stub.stubType)

    override fun createStub(psi: GdClassNaming, parentStub: StubElement<*>?): GdClassNamingStub {
        val inheritance = PsiTreeUtil.getStubChildOfType(psi, GdInheritance::class.java);

        return GdClassNamingStubImpl(parentStub, psi.classNameNmi?.name.orEmpty(), inheritance?.inheritancePath)
    }

}
