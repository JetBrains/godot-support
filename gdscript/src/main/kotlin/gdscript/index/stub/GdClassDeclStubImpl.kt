package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdClassDeclTl
import gdscript.psi.impl.GdClassDeclElementType

class GdClassDeclStubImpl : StubBase<GdClassDeclTl>, GdClassDeclStub {

    private var myClassname: String = ""
    private var myParentName: String = ""
    private var doc: GdCommentModel

    constructor(parent: StubElement<*>?, name: String, parentName: String?, doc: GdCommentModel) : super(parent, GdClassDeclElementType) {
        this.myClassname = name
        this.myParentName = parentName.orEmpty()
        this.doc = doc
    }

    override fun name(): String {
        return myClassname;
    }

    override fun parent(): String {
        return myParentName;
    }

    override fun description(): String = this.doc.description
    override fun brief(): String = this.doc.brief
    override fun tutorials(): List<GdTutorial> = this.doc.tutorials
    override fun isDeprecated(): Boolean = this.doc.isDeprecated
    override fun isExperimental(): Boolean = this.doc.isExperimental

}
