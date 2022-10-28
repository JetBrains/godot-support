package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.impl.GdEnumDeclElementType

class GdEnumDeclStubImpl : StubBase<GdEnumDeclTl>, GdEnumDeclStub {

    private var name: String = "";
    private var values: HashMap<String, Int> = HashMap();

    constructor(parent: StubElement<*>?, name: String?, values: HashMap<String, Int>): super(parent, GdEnumDeclElementType) {
        this.name = name.orEmpty();
        this.values = values;
    }

    override fun name(): String {
        return name;
    }

    override fun values(): HashMap<String, Int> {
        return values;
    }

}
