package tscn.index.stub

import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnGdResourceHeader

interface TscnGdResourceHeaderStub : StubElement<TscnGdResourceHeader> {

    fun getUid(): String

}
