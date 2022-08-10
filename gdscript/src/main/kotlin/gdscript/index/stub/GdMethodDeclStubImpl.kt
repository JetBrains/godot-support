package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.impl.GdMethodDeclElementType

class GdMethodDeclStubImpl : StubBase<GdMethodDeclTl>, GdMethodDeclStub {
    private var isStatic: Boolean = false;
    private var name: String = "";
    private var returnType: String = "";
    private var parameters: HashMap<String, String?> = HashMap();

    constructor(parent: StubElement<*>?, isStatic: Boolean, name: String?, returnType: String, parameters: HashMap<String, String?>): super(parent, GdMethodDeclElementType) {
        if (name != null) {
            this.name = name
        };
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    override fun isStatic(): Boolean {
        return isStatic;
    }

    override fun name(): String {
        return name;
    }

    override fun returnType(): String {
        return returnType;
    }

    override fun parameters(): HashMap<String, String?> {
        return parameters;
    }
}
