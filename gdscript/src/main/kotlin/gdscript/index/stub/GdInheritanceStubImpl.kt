package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdInheritance
import gdscript.psi.impl.GdInheritanceElementType

class GdInheritanceStubImpl : StubBase<GdInheritance>, GdInheritanceStub {
    private var myExtending: String = "";

    constructor(parent: StubElement<*>?, name: String?): super(parent, GdInheritanceElementType) {
        if (name != null) {
            this.myExtending = name
        };
    }

    override fun name(): String {
        return myExtending;
    }
}
