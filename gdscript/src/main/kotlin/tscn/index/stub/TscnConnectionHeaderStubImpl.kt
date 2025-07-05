package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnConnectionHeader
import tscn.psi.impl.TscnConnectionHeaderElementType

class TscnConnectionHeaderStubImpl : StubBase<TscnConnectionHeader>, TscnConnectionHeaderStub {

    private var from: String = "";
    private var to: String = "";
    private var signal: String = "";
    private var method: String = "";

    constructor(parent: StubElement<*>?, from: String, to: String, signal: String, method: String) : super(parent, TscnConnectionHeaderElementType) {
        this.from = from;
        this.to = to;
        this.signal = signal;
        this.method = method;
    }

    override fun getFrom(): String {
        return from;
    }

    override fun getTo(): String {
        return to;
    }

    override fun getMethod(): String {
        return method;
    }

    override fun getSignal(): String {
        return signal;
    }

}
