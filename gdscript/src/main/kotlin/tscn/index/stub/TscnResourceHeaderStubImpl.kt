package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnResourceHeader
import tscn.psi.impl.TscnResourceHeaderElementType

class TscnResourceHeaderStubImpl : StubBase<TscnResourceHeader>, TscnResourceHeaderStub {

    private var id: String = "";
    private var path: String = "";

    constructor(parent: StubElement<*>?, id: String, path: String) : super(parent, TscnResourceHeaderElementType) {
        this.id = id;
        this.path = path;
    }

    override fun getId(): String = id;

    override fun getPath(): String = path;

}
