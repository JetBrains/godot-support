package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnGdResourceHeader
import tscn.psi.impl.TscnGdResourceHeaderElementType

class TscnGdResourceHeaderStubImpl(
    parent: StubElement<*>?,
    private val uid: String,
) : StubBase<TscnGdResourceHeader>(parent, TscnGdResourceHeaderElementType), TscnGdResourceHeaderStub {

    override fun getUid(): String = uid

}
