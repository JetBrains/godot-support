package tscn.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnNodeHeaderStub
import tscn.index.stub.TscnNodeHeaderStubImpl
import tscn.psi.TscnNodeHeader

object TscnNodeHeaderElementType : IStubElementType<TscnNodeHeaderStub, TscnNodeHeader>("nodeHeader", TscnLanguage.INSTANCE) {

    @JvmStatic
    fun getInstance(debugName: String): TscnNodeHeaderElementType = TscnNodeHeaderElementType

    override fun getExternalId(): String = "tscn.nodeHeader"

    override fun serialize(stub: TscnNodeHeaderStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
        dataStream.writeName(stub.type());
        dataStream.writeName(stub.parentPath());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnNodeHeaderStub =
        TscnNodeHeaderStubImpl(
            parentStub,
            dataStream.readName()?.string ?: "",
            dataStream.readName()?.string ?: "",
            dataStream.readName()?.string ?: "",
        )

    override fun indexStub(stub: TscnNodeHeaderStub, sink: IndexSink) {
        sink.occurrence(TscnIndices.NODE_INDEX, stub.nodePath());
    }

    override fun createPsi(stub: TscnNodeHeaderStub): TscnNodeHeader =
        TscnNodeHeaderImpl(stub, stub.stubType);

    override fun createStub(psi: TscnNodeHeader, parentStub: StubElement<out PsiElement>?): TscnNodeHeaderStub =
        TscnNodeHeaderStubImpl(parentStub, psi.name, psi.type, psi.parentPath);

    override fun shouldCreateStub(node: ASTNode): Boolean {
        val element = node.psi as TscnNodeHeader;
        // Bez parenta to je node přímo daného objektu -> chci pouze potomky pro completion

        return element.parentPath.isNotEmpty();
    }

}
