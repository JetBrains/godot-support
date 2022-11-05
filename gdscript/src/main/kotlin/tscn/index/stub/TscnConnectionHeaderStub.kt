package tscn.index.stub

import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnConnectionHeader

interface TscnConnectionHeaderStub : StubElement<TscnConnectionHeader> {

    fun getFrom(): String;
    fun getTo(): String;
    fun getMethod(): String;
    fun getSignal(): String;

}
