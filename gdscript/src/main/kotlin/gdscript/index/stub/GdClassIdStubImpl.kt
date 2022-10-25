package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassNameNmi
import gdscript.psi.impl.GdClassIdElementType

class GdClassIdStubImpl : StubBase<GdClassNameNmi>, GdClassIdStub {
    private var myClassId: String = "";
    private var myParentName: String? = null;

    constructor(parent: StubElement<*>?, name: String, parentName: String?): super(parent, GdClassIdElementType) {
        this.myClassId = name
        if (parentName != null) {
            this.myParentName = parentName;
        };
    }

    override fun name(): String {
        return myClassId;
    }

    override fun parent(): String? {
        return myParentName;
    }

}
