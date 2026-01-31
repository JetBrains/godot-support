package tscn.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnConnectionHeaderStub
import tscn.index.stub.TscnConnectionHeaderStubImpl
import tscn.psi.TscnConnectionHeader

/**
 * Connection (signal) line
 * [connection signal="sig_c" from="." to="Outer/Uniqe/Node3D" method="_on_form_sig_c"]
 */
object TscnConnectionHeaderElementType : IStubElementType<TscnConnectionHeaderStub, TscnConnectionHeader>("connection", TscnLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): TscnConnectionHeaderElementType = TscnConnectionHeaderElementType

    override fun getExternalId(): String = "tscn.connection"

    override fun serialize(stub: TscnConnectionHeaderStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getFrom())
        dataStream.writeName(stub.getTo())
        dataStream.writeName(stub.getSignal())
        dataStream.writeName(stub.getMethod())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnConnectionHeaderStub =
        TscnConnectionHeaderStubImpl(
            parentStub,
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
        )

    override fun indexStub(stub: TscnConnectionHeaderStub, sink: IndexSink) {
        sink.occurrence(TscnIndices.CONNECTION_INDEX, stub.getMethod())
    }

    override fun createPsi(stub: TscnConnectionHeaderStub): TscnConnectionHeader =
        TscnConnectionHeaderImpl(stub, stub.stubType)

    override fun createStub(psi: TscnConnectionHeader, parentStub: StubElement<out PsiElement>?): TscnConnectionHeaderStub =
        TscnConnectionHeaderStubImpl(parentStub, psi.from, psi.to, psi.signal, psi.method)

}
