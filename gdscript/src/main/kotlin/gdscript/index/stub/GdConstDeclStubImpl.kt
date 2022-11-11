package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdConstDeclTl
import gdscript.psi.impl.GdConstDeclElementType

class GdConstDeclStubImpl : StubBase<GdConstDeclTl>, GdConstDeclStub {
    private var name: String = "";

    constructor(parent: StubElement<*>?, name: String?): super(parent, GdConstDeclElementType) {
        if (name != null) {
            this.name = name
        };
    }

    override fun name(): String {
        return name;
    }

}
