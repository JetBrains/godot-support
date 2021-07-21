package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassNaming
import gdscript.psi.impl.GdClassNamingElementType

class GdClassNamingStubImpl : StubBase<GdClassNaming>, GdClassNamingStub {
    private var myClassname: String = "";

    constructor(parent: StubElement<*>?, name: String?): super(parent, GdClassNamingElementType) {
        if (name != null) {
            this.myClassname = name
        };
    }

    override fun name(): String {
        return myClassname;
    }
}