package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.impl.GdMethodDeclElementType

class GdMethodDeclStubImpl : StubBase<GdMethodDeclTl>, GdMethodDeclStub {
    private var name: String = "";

    constructor(parent: StubElement<*>?, name: String?): super(parent, GdMethodDeclElementType) {
        if (name != null) {
            this.name = name
        };
    }

    override fun name(): String {
        return name;
    }
}
