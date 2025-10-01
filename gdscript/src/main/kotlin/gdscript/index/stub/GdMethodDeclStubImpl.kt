package gdscript.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.impl.GdMethodDeclElementType

class GdMethodDeclStubImpl : StubBase<GdMethodDeclTl>, GdMethodDeclStub {

    private var isStatic: Boolean = false
    private var isVariadic: Boolean = false
    private var name: String = ""
    private var returnType: String = ""
    private var isConstructor: Boolean = false
    private var parameters: LinkedHashMap<String, String?> = LinkedHashMap()
    private var doc: GdCommentModel

    constructor(
        parent: StubElement<*>?,
        isStatic: Boolean,
        isVariadic: Boolean,
        isConstructor: Boolean,
        name: String?,
        returnType: String,
        parameters: LinkedHashMap<String, String?>,
        doc: GdCommentModel,
    ) : super(parent, GdMethodDeclElementType) {
        if (name != null) {
            this.name = name
        }
        this.isStatic = isStatic
        this.isVariadic = isVariadic
        this.isConstructor = isConstructor
        this.returnType = returnType
        this.parameters = parameters
        this.doc = doc
    }

    override fun isStatic(): Boolean = isStatic

    override fun isVariadic(): Boolean = isVariadic

    override fun name(): String = name

    override fun returnType(): String = returnType

    override fun parameters(): LinkedHashMap<String, String?> = parameters

    override fun isConstructor(): Boolean = isConstructor

    override fun description(): String = this.doc.description
    override fun brief(): String = this.doc.brief
    override fun tutorials(): List<GdTutorial> = this.doc.tutorials
    override fun isDeprecated(): Boolean = this.doc.isDeprecated
    override fun isExperimental(): Boolean = this.doc.isExperimental

}
