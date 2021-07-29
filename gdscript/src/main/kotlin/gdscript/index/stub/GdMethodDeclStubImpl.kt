package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.impl.GdMethodDeclElementType

class GdMethodDeclStubImpl : StubBase<GdMethodDeclTl>, GdMethodDeclStub {
    private var name: String = "";
    private var returnType: String? = null;
    private var parameters: HashMap<String, String?> = HashMap();

    constructor(parent: StubElement<*>?, name: String?, returnType: String?, parameters: HashMap<String, String?>): super(parent, GdMethodDeclElementType) {
        if (name != null) {
            this.name = name
        };
        this.returnType = if (returnType == "") null else returnType;
        this.parameters = parameters;
    }

    override fun name(): String {
        return name;
    }

    override fun returnType(): String? {
        return returnType;
    }

    override fun parameters(): HashMap<String, String?> {
        return parameters;
    }
}
