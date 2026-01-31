package config.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import config.GdConfigLanguage
import config.index.GdConfigIndices
import config.index.stub.ConfigOperatorStub
import config.index.stub.ConfigOperatorStubImpl
import config.psi.ConfigOperator

object GdConfigOperatorElementType : IStubElementType<ConfigOperatorStub, ConfigOperator>("OPERATOR", GdConfigLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdConfigOperatorElementType = GdConfigOperatorElementType

    override fun getExternalId(): String {
        return "gdconf.OPERATOR"
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): ConfigOperatorStub {
        return ConfigOperatorStubImpl(
            parentStub,
            dataStream.readNameString() ?: "",
        )
    }

    override fun createStub(psi: ConfigOperator, parentStub: StubElement<out PsiElement>?): ConfigOperatorStub {
        return ConfigOperatorStubImpl(
            parentStub,
            psi.name,
        )
    }

    override fun createPsi(stub: ConfigOperatorStub): ConfigOperator {
        return ConfigOperatorImpl(stub, stub.stubType)
    }

    override fun indexStub(stub: ConfigOperatorStub, sink: IndexSink) {
        sink.occurrence(GdConfigIndices.OPERATOR_INDEX, stub.name())
    }

    override fun serialize(stub: ConfigOperatorStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name())
    }

}
