package gdscript.psi.impl

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdClassIdStub
import gdscript.index.stub.GdClassIdStubImpl
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.psi.GdInheritance
import gdscript.psi.utils.PsiGdClassUtil
import gdscript.psi.utils.PsiGdResourceUtil
import gdscript.psi.utils.PsiGdTreeUtil

object GdClassIdElementType : IStubElementType<GdClassIdStub, GdClassNameNmi>("gd.classId", GdLanguage) {

    fun getClassId(element: GdClassNameNmi): String {
        val parents: MutableList<String> = mutableListOf()
        var parent = PsiGdClassUtil.getParentClassElement(element)
        while (true) {
            if (parent is GdClassDeclTl) {
                parents.add(parent.classNameNmi?.name.orEmpty())
            } else if (parent is GdClassNaming) {
                parents.add(parent.classNameNmi?.name.orEmpty())
                break
            } else if (parent is GdFile) {
                parents.add("\"${PsiGdResourceUtil.resourcePath(parent.virtualFile ?: parent.originalFile.virtualFile)}\"")
                break
            } else {
                break
            }

            parent = PsiGdClassUtil.getParentClassElement(parent)
        }

        return parents.reversed().joinToString(".")
    }

    @Deprecated("this should be at declaration, not here.. ?")
    fun getParentName(element: GdClassNameNmi): String? {
        val declaration = PsiGdTreeUtil.findFirstPrecedingElement(element) {
            it is GdClassDeclTl || it is GdClassNaming
        }

        val inh = PsiTreeUtil.findChildOfType(declaration, GdInheritance::class.java)

        return inh?.inheritancePath
    }

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdClassIdElementType {
        return GdClassIdElementType
    }

    override fun getExternalId(): String = "GdScript.classId"

    override fun serialize(stub: GdClassIdStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name())
        dataStream.writeName(stub.parent())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdClassIdStub {
        return GdClassIdStubImpl(parentStub, dataStream.readNameString()!!, dataStream.readNameString())
    }

    override fun indexStub(stub: GdClassIdStub, sink: IndexSink) {
        sink.occurrence(Indices.CLASS_NAME_ID, stub.name())
    }

    override fun createPsi(stub: GdClassIdStub): GdClassNameNmi =
            GdClassNameNmiImpl(stub, stub.stubType)

    override fun createStub(psi: GdClassNameNmi, parentStub: StubElement<*>?): GdClassIdStub {
        return GdClassIdStubImpl(parentStub, psi.classId, getParentName(psi))
    }

}
