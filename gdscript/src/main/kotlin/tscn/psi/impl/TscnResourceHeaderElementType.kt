package tscn.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import tscn.TscnLanguage
import tscn.index.TscnIndices
import tscn.index.stub.TscnResourceHeaderStub
import tscn.index.stub.TscnResourceHeaderStubImpl
import tscn.psi.TscnResourceHeader

/**
 * Resource line (storing only scripts)
 * [ext_resource type="Script" path="res://Asd.gd" id="1_3apro"]
 */
object TscnResourceHeaderElementType : IStubElementType<TscnResourceHeaderStub, TscnResourceHeader>("extResource", TscnLanguage) {

    val TO_INDEX = arrayOf("Script", "PackedScene")

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): TscnResourceHeaderElementType = TscnResourceHeaderElementType

    override fun getExternalId(): String = "tscn.extResource"

    override fun serialize(stub: TscnResourceHeaderStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getId());
        dataStream.writeName(stub.getPath());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TscnResourceHeaderStub =
        TscnResourceHeaderStubImpl(
            parentStub,
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
        )

    override fun indexStub(stub: TscnResourceHeaderStub, sink: IndexSink) {
        sink.occurrence(TscnIndices.RESOURCE_INDEX, stub.getPath());
    }

    override fun createPsi(stub: TscnResourceHeaderStub): TscnResourceHeader =
        TscnResourceHeaderImpl(stub, stub.stubType);

    override fun createStub(psi: TscnResourceHeader, parentStub: StubElement<out PsiElement>?): TscnResourceHeaderStub =
        TscnResourceHeaderStubImpl(parentStub, psi.id, psi.path);

    override fun shouldCreateStub(node: ASTNode): Boolean {
        val element = node.psi as TscnResourceHeader;

        return TO_INDEX.contains(element.type)
    }

}
