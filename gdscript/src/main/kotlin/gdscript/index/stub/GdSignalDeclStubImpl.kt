package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.impl.GdSignalDeclElementType

class GdSignalDeclStubImpl : StubBase<GdSignalDeclTl>, GdSignalDeclStub {
    private var name: String = ""
    private var parameters: LinkedHashMap<String, String?>
    private var doc: GdCommentModel

    constructor(parent: StubElement<*>?, name: String?, parameters: LinkedHashMap<String, String?>, doc: GdCommentModel): super(parent, GdSignalDeclElementType) {
        if (name != null) {
            this.name = name
        };
        this.parameters = parameters
        this.doc = doc
    }

    override fun name(): String {
        return name;
    }

    override fun parameters(): LinkedHashMap<String, String?> {
        return parameters
    }

    override fun description(): String = this.doc.description
    override fun brief(): String = this.doc.brief
    override fun tutorials(): List<GdTutorial> = this.doc.tutorials
    override fun isDeprecated(): Boolean = this.doc.isDeprecated
    override fun isExperimental(): Boolean = this.doc.isExperimental

}
