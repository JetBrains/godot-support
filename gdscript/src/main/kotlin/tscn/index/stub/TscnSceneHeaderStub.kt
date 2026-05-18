package tscn.index.stub

import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnSceneHeader

interface TscnSceneHeaderStub : StubElement<TscnSceneHeader> {

    fun getUid(): String

}
