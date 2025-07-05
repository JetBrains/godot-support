package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.impl.GdEnumDeclElementType

class GdEnumDeclStubImpl : StubBase<GdEnumDeclTl>, GdEnumDeclStub {

    private var name: String = ""
    private var values: HashMap<String, Long> = HashMap()
    private var doc: GdCommentModel

    constructor(parent: StubElement<*>?, name: String?, values: HashMap<String, Long>, doc: GdCommentModel): super(parent, GdEnumDeclElementType) {
        this.name = name.orEmpty();
        this.values = values
        this.doc = doc
    }

    override fun name(): String {
        return name
    }

    override fun values(): HashMap<String, Long> {
        return values
    }

    override fun description(): String = this.doc.description
    override fun brief(): String = this.doc.brief
    override fun tutorials(): List<GdTutorial> = this.doc.tutorials
    override fun isDeprecated(): Boolean = this.doc.isDeprecated
    override fun isExperimental(): Boolean = this.doc.isExperimental

}
