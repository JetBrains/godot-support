package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnParagraph
import tscn.psi.impl.TscnParagraphElementType

class TscnParagraphStubImpl : StubBase<TscnParagraph>, TscnParagraphStub {

    private var uid: String = ""

    constructor(parent: StubElement<*>?, uid: String) : super(
        parent,
        TscnParagraphElementType
    ) {
        this.uid = uid
    }

    override fun getUid(): String = uid

}
