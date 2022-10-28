package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassNaming
import gdscript.psi.impl.GdClassNamingElementType

class GdClassNamingStubImpl : StubBase<GdClassNaming>, GdClassNamingStub {

    private var myClassname: String = "";
    private var myParentName: String = "";

    constructor(parent: StubElement<*>?, name: String, parentName: String?) : super(parent, GdClassNamingElementType) {
        this.myClassname = name;
        this.myParentName = parentName.orEmpty();
    }

    override fun name(): String {
        return myClassname;
    }

    override fun parent(): String {
        return myParentName;
    }

}
