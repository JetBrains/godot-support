package tscn.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.jetbrains.rider.godot.community.tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnNodeHeaderStub
import tscn.index.stub.TscnNodeHeaderStubImpl
import tscn.psi.TscnNodeHeader

object TscnNodeHeaderElementType :
    IStubElementType<TscnNodeHeaderStub, TscnNodeHeader>("nodeHeader", TscnLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): TscnNodeHeaderElementType = TscnNodeHeaderElementType

    override fun getExternalId(): String = "tscn.nodeHeader"

    override fun serialize(stub: TscnNodeHeaderStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getName())
        dataStream.writeName(stub.getType())
        dataStream.writeName(stub.getParentPath())
        dataStream.writeName(stub.getNodePath())
        dataStream.writeBoolean(stub.isUniqueNameOwner())
        dataStream.writeBoolean(stub.isVisible())
        dataStream.writeName(stub.getScriptResource())
        dataStream.writeName(stub.getInstanceResource())
        dataStream.writeNames(stub.getGroups())
        dataStream.writeNames(stub.getMetas())
        dataStream.writeInt(stub.getIndex())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnNodeHeaderStub =
        TscnNodeHeaderStubImpl(
            parentStub,
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
            dataStream.readBoolean(),
            dataStream.readBoolean(),
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
            dataStream.readNames(),
            dataStream.readNames(),
            dataStream.readInt(),
        )

    private fun StubOutputStream.writeNames(names: Array<String>) {
        writeInt(names.size)
        names.forEach { writeName(it) }
    }

    private fun StubInputStream.readNames(): Array<String> =
        Array(readInt()) { readNameString().orEmpty() }

    override fun indexStub(stub: TscnNodeHeaderStub, sink: IndexSink) {
        sink.occurrence(TscnIndices.NODE_INDEX, stub.getNodePath())
    }

    override fun createPsi(stub: TscnNodeHeaderStub): TscnNodeHeader =
        TscnNodeHeaderImpl(stub, stub.stubType)

    override fun createStub(psi: TscnNodeHeader, parentStub: StubElement<out PsiElement>?): TscnNodeHeaderStub =
        TscnNodeHeaderStubImpl(
            parentStub,
            psi.name,
            psi.type,
            psi.parentPath,
            psi.nodePath,
            psi.isUniqueNameOwner,
            psi.isVisible,
            psi.scriptResource,
            psi.instanceResource,
            psi.groups,
            psi.metas,
            psi.index,
        )

}
