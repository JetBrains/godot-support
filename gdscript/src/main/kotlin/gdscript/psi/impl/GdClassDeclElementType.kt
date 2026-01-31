package gdscript.psi.impl

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdClassDeclStub
import gdscript.index.stub.GdClassDeclStubImpl
import gdscript.model.GdCommentModel
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdInheritance
import gdscript.psi.utils.GdCommentUtil

object GdClassDeclElementType : IStubElementType<GdClassDeclStub, GdClassDeclTl>("classDecl", GdLanguage) {

    /**
     * @return name of extended class
     */
    fun getParentName(element: GdClassDeclTl): String {
        val stub = element.stub
        if (stub !== null) {
            return stub.parent()
        }

        return PsiTreeUtil.getStubChildOfType(element, GdInheritance::class.java)?.inheritancePath.orEmpty()
    }

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdClassDeclElementType {
        return GdClassDeclElementType
    }

    override fun getExternalId(): String = "GdScript.classDecl"

    override fun serialize(stub: GdClassDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name())
        dataStream.writeName(stub.parent())
        GdCommentModel.serializeDocumentation(stub, dataStream)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdClassDeclStub {
        return GdClassDeclStubImpl(
            parentStub,
            dataStream.readNameString().orEmpty(),
            dataStream.readName()?.string,
            GdCommentModel(dataStream),
        )
    }

    override fun indexStub(stub: GdClassDeclStub, sink: IndexSink) {
        sink.occurrence(Indices.CLASS_DECL, stub.name())
    }

    override fun createPsi(stub: GdClassDeclStub): GdClassDeclTl =
        GdClassDeclTlImpl(stub, stub.stubType)

    override fun createStub(psi: GdClassDeclTl, parentStub: StubElement<*>?): GdClassDeclStub {
        val inheritance = PsiTreeUtil.getStubChildOfType(psi, GdInheritance::class.java)

        return GdClassDeclStubImpl(
            parentStub,
            psi.classNameNmi?.name.orEmpty(),
            inheritance?.inheritancePath,
            GdCommentUtil.collectComments(psi),
        )
    }

}
