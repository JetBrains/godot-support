package tscn.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnExtHeaderStub
import tscn.index.stub.TscnExtHeaderStubImpl
import tscn.psi.TscnExtHeader

object TscnExtHeaderElementType : IStubElementType<TscnExtHeaderStub, TscnExtHeader>("extHeader", TscnLanguage.INSTANCE) {

    @JvmStatic
    fun getInstance(debugName: String): TscnExtHeaderElementType = TscnExtHeaderElementType

    override fun getExternalId(): String = "tscn.extHeader"

    override fun serialize(stub: TscnExtHeaderStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.path());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnExtHeaderStub =
        TscnExtHeaderStubImpl(parentStub, dataStream.readName()?.string ?: "")

    override fun indexStub(stub: TscnExtHeaderStub, sink: IndexSink) {
        sink.occurrence(TscnIndices.SCRIPT_INDEX, stub.path());
    }

    override fun createPsi(stub: TscnExtHeaderStub): TscnExtHeader =
        TscnExtHeaderImpl(stub, stub.stubType);

    override fun createStub(psi: TscnExtHeader, parentStub: StubElement<out PsiElement>?): TscnExtHeaderStub =
        TscnExtHeaderStubImpl(parentStub, psi.path);

    override fun shouldCreateStub(node: ASTNode): Boolean {
        val element = node.psi as TscnExtHeader;

        return element.type == "Script";
    }

}
