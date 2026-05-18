package tscn.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.jetbrains.rider.godot.community.tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnGdResourceHeaderStub
import tscn.index.stub.TscnGdResourceHeaderStubImpl
import tscn.psi.TscnGdResourceHeader

object TscnGdResourceHeaderElementType : IStubElementType<TscnGdResourceHeaderStub, TscnGdResourceHeader>("gdResourceHeader", TscnLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): TscnGdResourceHeaderElementType = TscnGdResourceHeaderElementType

    override fun getExternalId(): String = "tscn.gdResourceHeader"

    override fun serialize(stub: TscnGdResourceHeaderStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getUid())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnGdResourceHeaderStub =
        TscnGdResourceHeaderStubImpl(parentStub, dataStream.readNameString() ?: "")

    override fun indexStub(stub: TscnGdResourceHeaderStub, sink: IndexSink) {
        val uid = stub.getUid()
        if (uid.isNotEmpty()) {
            sink.occurrence(TscnIndices.UID_INDEX, uid)
        }
    }

    override fun createPsi(stub: TscnGdResourceHeaderStub): TscnGdResourceHeader =
        TscnGdResourceHeaderImpl(stub, stub.stubType)

    override fun createStub(psi: TscnGdResourceHeader, parentStub: StubElement<out PsiElement>?): TscnGdResourceHeaderStub =
        TscnGdResourceHeaderStubImpl(parentStub, psi.uid)

}
