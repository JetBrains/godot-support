package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdInheritance
import gdscript.psi.impl.GdInheritanceElementType

class GdInheritanceStubImpl : StubBase<GdInheritance>, GdInheritanceStub {
    private var inheritancePath: String = "";

    constructor(parent: StubElement<*>?, path: String): super(parent, GdInheritanceElementType) {
        this.inheritancePath = path;
    }

    override fun inheritancePath(): String {
        return inheritancePath;
    }
}
