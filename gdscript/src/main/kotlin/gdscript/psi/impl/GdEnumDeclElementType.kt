package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdEnumDeclStub
import gdscript.index.stub.GdEnumDeclStubImpl
import gdscript.model.GdCommentModel
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdCommentUtil
import gdscript.psi.utils.PsiGdEnumUtil

object GdEnumDeclElementType : IStubElementType<GdEnumDeclStub, GdEnumDeclTl>("enumDecl", GdLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdEnumDeclElementType {
        return GdEnumDeclElementType
    }

    override fun getExternalId(): String = "GdScript.enumDecl"

    // Only create a stub for named enums
    override fun shouldCreateStub(node: ASTNode): Boolean {
        return node.findChildByType(GdTypes.ENUM_DECL_NMI) != null
    }

    override fun serialize(stub: GdEnumDeclStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name())
        dataStream.writeName(stub.values().toString())
        GdCommentModel.serializeDocumentation(stub, dataStream)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdEnumDeclStub =
        GdEnumDeclStubImpl(
            parentStub,
            dataStream.readNameString(),
            PsiGdEnumUtil.fromString(dataStream.readNameString()),
            GdCommentModel(dataStream),
        )

    override fun indexStub(stub: GdEnumDeclStub, sink: IndexSink) {
        val name = stub.name()
        if (name.isNotEmpty()) {
            sink.occurrence(Indices.ENUM, name)
        }
    }

    override fun createPsi(stub: GdEnumDeclStub): GdEnumDeclTl =
        GdEnumDeclTlImpl(stub, stub.stubType)

    override fun createStub(psi: GdEnumDeclTl, parentStub: StubElement<*>?): GdEnumDeclStub {
        return GdEnumDeclStubImpl(
            parentStub, psi.enumDeclNmi?.name, psi.values,
            GdCommentUtil.collectComments(psi),
        )
    }

}
