package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnExtHeader
import tscn.psi.impl.TscnExtHeaderElementType

class TscnExtHeaderStubImpl : StubBase<TscnExtHeader>, TscnExtHeaderStub {

    private var path: String = "";

    constructor(parent: StubElement<*>?, path: String) : super(parent, TscnExtHeaderElementType) {
        this.path = path;
    }

    override fun path(): String = path;

}
