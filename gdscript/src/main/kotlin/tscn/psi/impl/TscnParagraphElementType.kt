package tscn.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.jetbrains.rider.godot.community.tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnParagraphStub
import tscn.index.stub.TscnParagraphStubImpl
import tscn.psi.TscnParagraph
import tscn.psi.TscnSceneHeader
import tscn.psi.TscnUnknownHeader
import tscn.psi.utils.TscnHeaderUtils

object TscnParagraphElementType :
    IStubElementType<TscnParagraphStub, TscnParagraph>("paragraph", TscnLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): TscnParagraphElementType = TscnParagraphElementType

    override fun getExternalId(): String = "tscn.paragraph"

    override fun serialize(stub: TscnParagraphStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getUid())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnParagraphStub =
        TscnParagraphStubImpl(parentStub, dataStream.readNameString() ?: "")

    override fun indexStub(stub: TscnParagraphStub, sink: IndexSink) {
        sink.occurrence(TscnIndices.PARAGRAPH_INDEX, "paragraph")
        val uid = stub.getUid()
        if (uid.isNotEmpty()) {
            sink.occurrence(TscnIndices.SCENE_UID_INDEX, uid)
        }
    }

    override fun createPsi(stub: TscnParagraphStub): TscnParagraph =
        TscnParagraphImpl(stub, stub.stubType)

    override fun createStub(psi: TscnParagraph, parentStub: StubElement<out PsiElement>?): TscnParagraphStub {
        val header = psi.header
        var uid = ""
        if (header is TscnSceneHeader || header is TscnUnknownHeader) {
            val values = when (header) {
                is TscnSceneHeader -> header.headerValueList
                is TscnUnknownHeader -> header.headerValueList
                else -> emptyList()
            }
            uid = TscnHeaderUtils.getValue(values, TscnHeaderUtils.HL_UID)
        }
        return TscnParagraphStubImpl(parentStub, uid)
    }

}
