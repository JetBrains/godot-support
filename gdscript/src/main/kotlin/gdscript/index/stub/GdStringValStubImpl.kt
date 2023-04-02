package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdStringVal
import gdscript.psi.impl.GdStringValElementType

class GdStringValStubImpl : StubBase<GdStringVal>, GdStringValStub {
    private var name: String = ""

    constructor(parent: StubElement<*>?, name: String?): super(parent, GdStringValElementType) {
        if (name != null) this.name = name
    }

    override fun name(): String {
        return name
    }

}
