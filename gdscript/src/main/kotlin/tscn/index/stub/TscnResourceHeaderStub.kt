package tscn.index.stub

import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnResourceHeader

interface TscnResourceHeaderStub : StubElement<TscnResourceHeader> {

    fun getId(): String
    fun getPath(): String

}
