package tscn.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.jetbrains.rider.godot.community.tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnSceneHeaderStub
import tscn.index.stub.TscnSceneHeaderStubImpl
import tscn.psi.TscnSceneHeader

object TscnSceneHeaderElementType : IStubElementType<TscnSceneHeaderStub, TscnSceneHeader>("sceneHeader", TscnLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): TscnSceneHeaderElementType = TscnSceneHeaderElementType

    override fun getExternalId(): String = "tscn.sceneHeader"

    override fun serialize(stub: TscnSceneHeaderStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getUid())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnSceneHeaderStub =
        TscnSceneHeaderStubImpl(parentStub, dataStream.readNameString() ?: "")

    override fun indexStub(stub: TscnSceneHeaderStub, sink: IndexSink) {
        val uid = stub.getUid()
        if (uid.isNotEmpty()) {
            sink.occurrence(TscnIndices.UID_INDEX, uid)
        }
    }

    override fun createPsi(stub: TscnSceneHeaderStub): TscnSceneHeader =
        TscnSceneHeaderImpl(stub, stub.stubType)

    override fun createStub(psi: TscnSceneHeader, parentStub: StubElement<out PsiElement>?): TscnSceneHeaderStub =
        TscnSceneHeaderStubImpl(parentStub, psi.uid)

}
