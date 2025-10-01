package tscn.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnParagraphStub
import tscn.index.stub.TscnParagraphStubImpl
import tscn.psi.TscnParagraph

object TscnParagraphElementType :
    IStubElementType<TscnParagraphStub, TscnParagraph>("paragraph", TscnLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): TscnParagraphElementType = TscnParagraphElementType

    override fun getExternalId(): String = "tscn.paragraph"

    override fun serialize(stub: TscnParagraphStub, dataStream: StubOutputStream) {}

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnParagraphStub =
        TscnParagraphStubImpl(parentStub)

    override fun indexStub(stub: TscnParagraphStub, sink: IndexSink) {
        sink.occurrence(TscnIndices.PARAGRAPH_INDEX, "paragraph")
    }

    override fun createPsi(stub: TscnParagraphStub): TscnParagraph =
        TscnParagraphImpl(stub, stub.stubType)

    override fun createStub(psi: TscnParagraph, parentStub: StubElement<out PsiElement>?): TscnParagraphStub =
        TscnParagraphStubImpl(parentStub)

}
