package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassDeclTl
import gdscript.psi.impl.GdClassDeclElementType

class GdClassDeclStubImpl : StubBase<GdClassDeclTl>, GdClassDeclStub {

    private var myClassname: String = "";
    private var myParentName: String = "";

    constructor(parent: StubElement<*>?, name: String, parentName: String?) : super(parent, GdClassDeclElementType) {
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
