package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnSceneHeader
import tscn.psi.impl.TscnSceneHeaderElementType

class TscnSceneHeaderStubImpl(
    parent: StubElement<*>?,
    private val uid: String,
) : StubBase<TscnSceneHeader>(parent, TscnSceneHeaderElementType), TscnSceneHeaderStub {

    override fun getUid(): String = uid

}
