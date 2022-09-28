package tscn.index.stub

import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnNodeHeader

interface TscnNodeHeaderStub : StubElement<TscnNodeHeader> {

    fun name(): String;
    fun type(): String;
    fun parentPath(): String;
    fun nodePath(): String;
    fun isUniqueNameOwner(): Boolean;

}
