package config.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import common.util.HashMapUtil.fromSimpleParamString
import common.util.HashMapUtil.toSimpleParamString
import config.GdConfigLanguage
import config.index.GdConfigIndices
import config.index.stub.ConfigAnnotationStub
import config.index.stub.ConfigAnnotationStubImpl
import config.psi.ConfigAnnotation

object GdConfigAnnotationElementType : IStubElementType<ConfigAnnotationStub, ConfigAnnotation>("annotation", GdConfigLanguage) {

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdConfigAnnotationElementType = GdConfigAnnotationElementType

    override fun getExternalId(): String {
        return "gdconf.annotation"
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): ConfigAnnotationStub {
        return ConfigAnnotationStubImpl(
            parentStub,
            dataStream.readBoolean(),
            dataStream.readInt(),
            dataStream.readNameString() ?: "",
            (dataStream.readNameString() ?: "").fromSimpleParamString()
        )
    }

    override fun createStub(psi: ConfigAnnotation, parentStub: StubElement<out PsiElement>?): ConfigAnnotationStub {
        return ConfigAnnotationStubImpl(
            parentStub,
            psi.isVariadic,
            psi.requiredCount(),
            psi.name,
            psi.params,
        )
    }

    override fun createPsi(stub: ConfigAnnotationStub): ConfigAnnotation {
        return ConfigAnnotationImpl(stub, stub.stubType)
    }

    override fun indexStub(stub: ConfigAnnotationStub, sink: IndexSink) {
        sink.occurrence(GdConfigIndices.ANNOTATION_INDEX, stub.name())
    }

    override fun serialize(stub: ConfigAnnotationStub, dataStream: StubOutputStream) {
        dataStream.writeBoolean(stub.isVariadic())
        dataStream.writeInt(stub.requiredCount())
        dataStream.writeName(stub.name())
        dataStream.writeName(stub.params().toSimpleParamString())
    }

}
