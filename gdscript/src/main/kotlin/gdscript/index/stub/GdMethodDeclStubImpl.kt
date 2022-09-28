package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.impl.GdMethodDeclElementType

class GdMethodDeclStubImpl : StubBase<GdMethodDeclTl>, GdMethodDeclStub {
    private var isStatic: Boolean = false;
    private var name: String = "";
    private var returnType: String = "";
    private var isConstructor: Boolean = false;
    private var parameters: HashMap<String, String?> = HashMap();

    constructor(
        parent: StubElement<*>?,
        isStatic: Boolean,
        isConstructor: Boolean,
        name: String?,
        returnType: String,
        parameters: HashMap<String, String?>,
    ) : super(parent, GdMethodDeclElementType) {
        if (name != null) {
            this.name = name
        };
        this.isStatic = isStatic;
        this.isConstructor = isConstructor;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    override fun isStatic(): Boolean = isStatic;

    override fun name(): String = name;

    override fun returnType(): String = returnType;

    override fun parameters(): HashMap<String, String?> = parameters;

    override fun isConstructor(): Boolean = isConstructor;
}
