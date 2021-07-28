package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassNaming
import gdscript.psi.impl.GdClassNamingElementType

class GdClassNamingStubImpl : StubBase<GdClassNaming>, GdClassNamingStub {
    private var myClassname: String = "";
    private var myParentName: String = "";

    constructor(parent: StubElement<*>?, name: String?, parentName: String?): super(parent, GdClassNamingElementType) {
        if (name != null) {
            this.myClassname = name
        };
        if (parentName != null) {
            this.myParentName = parentName;
        };
    }

    override fun name(): String {
        return myClassname;
    }

    override fun parent(): String {
        return myParentName;
    }
}