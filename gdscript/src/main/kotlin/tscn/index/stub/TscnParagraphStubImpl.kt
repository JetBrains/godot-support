package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnParagraph
import tscn.psi.impl.TscnParagraphElementType

class TscnParagraphStubImpl : StubBase<TscnParagraph>, TscnParagraphStub {

    constructor(parent: StubElement<*>?) : super(
        parent,
        TscnParagraphElementType
    ) {
    }

}
