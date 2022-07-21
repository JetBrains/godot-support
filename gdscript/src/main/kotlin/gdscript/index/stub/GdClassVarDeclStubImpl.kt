package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.impl.GdClassVarDeclElementType

class GdClassVarDeclStubImpl : StubBase<GdClassVarDeclTl>, GdClassVarDeclStub {

    private var name: String = "";
    private var returnType: String = "";

    constructor(parent: StubElement<*>?, name: String?, returnType: String): super(parent, GdClassVarDeclElementType) {
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
