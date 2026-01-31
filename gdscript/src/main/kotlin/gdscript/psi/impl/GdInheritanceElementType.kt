package gdscript.psi.impl

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdInheritanceStub
import gdscript.index.stub.GdInheritanceStubImpl
import gdscript.psi.GdInheritance

object GdInheritanceElementType : IStubElementType<GdInheritanceStub, GdInheritance>("inheritance", GdLanguage) {

    fun inheritancePath(element: GdInheritance): String {
        return element.inheritanceId?.text.orEmpty()
    }

    @JvmStatic
    fun getInstance(@Suppress("UNUSED_PARAMETER") debugName: String): GdInheritanceElementType {
        return GdInheritanceElementType
    }

    override fun getExternalId(): String = "GdScript.inheritance"

    override fun serialize(stub: GdInheritanceStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.inheritancePath())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdInheritanceStub =
        GdInheritanceStubImpl(parentStub, dataStream.readNameString().orEmpty())

    override fun indexStub(stub: GdInheritanceStub, sink: IndexSink) {
        sink.occurrence(Indices.INHERITANCE, stub.inheritancePath())
    }

    override fun createPsi(stub: GdInheritanceStub): GdInheritance =
        GdInheritanceImpl(stub, stub.stubType)

    override fun createStub(psi: GdInheritance, parentStub: StubElement<*>?): GdInheritanceStub {
        return GdInheritanceStubImpl(parentStub, psi.inheritanceId?.text.orEmpty())
    }

}
