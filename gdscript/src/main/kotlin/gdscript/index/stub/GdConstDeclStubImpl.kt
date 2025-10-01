package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdConstDeclTl
import gdscript.psi.impl.GdConstDeclElementType

class GdConstDeclStubImpl : StubBase<GdConstDeclTl>, GdConstDeclStub {
    private var name: String = ""
    private var doc: GdCommentModel

    constructor(parent: StubElement<*>?, name: String?, doc: GdCommentModel): super(parent, GdConstDeclElementType) {
        if (name != null) {
            this.name = name
        }
        this.doc = doc
    }

    override fun name(): String {
        return name
    }

    override fun description(): String = this.doc.description
    override fun brief(): String = this.doc.brief
    override fun tutorials(): List<GdTutorial> = this.doc.tutorials
    override fun isDeprecated(): Boolean = this.doc.isDeprecated
    override fun isExperimental(): Boolean = this.doc.isExperimental

}
