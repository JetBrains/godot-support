package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.impl.GdSignalDeclElementType

class GdSignalDeclStubImpl : StubBase<GdSignalDeclTl>, GdSignalDeclStub {
    private var name: String = "";
    private var parameters: Array<String>;

    constructor(parent: StubElement<*>?, name: String?, parameters: Array<String>): super(parent, GdSignalDeclElementType) {
        if (name != null) {
            this.name = name
        };
        this.parameters = parameters;
    }

    override fun name(): String {
        return name;
    }

    override fun parameters(): Array<String> {
        return parameters;
    }
}
