package config.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import config.GdConfigLanguage
import config.index.GdConfigIndices
import config.index.stub.ConfigOperationStub
import config.index.stub.ConfigOperationStubImpl
import config.psi.ConfigOperation

object GdConfigOperationElementType : IStubElementType<ConfigOperationStub, ConfigOperation>("OPERATION", GdConfigLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdConfigOperationElementType = GdConfigOperationElementType

    override fun getExternalId(): String {
        return "gdconf.OPERATION"
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): ConfigOperationStub {
        return ConfigOperationStubImpl(
            parentStub,
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
            dataStream.readNameString() ?: "",
        )
    }

    override fun createStub(psi: ConfigOperation, parentStub: StubElement<out PsiElement>?): ConfigOperationStub {
        return ConfigOperationStubImpl(
            parentStub,
            psi.operand,
            psi.leftTyped,
            psi.rightTyped,
        )
    }

    override fun createPsi(stub: ConfigOperationStub): ConfigOperation {
        return ConfigOperationImpl(stub, stub.stubType)
    }

    override fun indexStub(stub: ConfigOperationStub, sink: IndexSink) {
        sink.occurrence(GdConfigIndices.OPERATION_INDEX, stub.leftTyped())
    }

    override fun serialize(stub: ConfigOperationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.operand())
        dataStream.writeName(stub.leftTyped())
        dataStream.writeName(stub.rightTyped())
    }

}
