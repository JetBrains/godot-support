package tscn.index.stub

import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnExtHeader

interface TscnExtHeaderStub : StubElement<TscnExtHeader> {

    fun path(): String;

}
