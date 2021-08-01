package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdConstDeclTl
import gdscript.psi.impl.GdConstDeclElementType

class GdConstDeclStubImpl : StubBase<GdConstDeclTl>, GdConstDeclStub {
    private var name: String = "";
    private var returnType: String = "";

    constructor(parent: StubElement<*>?, name: String?, returnType: String): super(parent, GdConstDeclElementType) {
        if (name != null) {
            this.name = name
        };
        this.returnType = returnType;
    }

    override fun name(): String {
        return name;
    }

    override fun returnType(): String {
        return returnType;
    }
}
