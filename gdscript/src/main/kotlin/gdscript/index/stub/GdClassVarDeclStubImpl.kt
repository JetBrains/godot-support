package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.impl.GdClassVarDeclElementType

class GdClassVarDeclStubImpl : StubBase<GdClassVarDeclTl>, GdClassVarDeclStub {

    private var name: String = "";

    constructor(parent: StubElement<*>?, name: String?): super(parent, GdClassVarDeclElementType) {
        if (name != null) {
            this.name = name
        };
    }

    override fun name(): String {
        return name;
    }

}
