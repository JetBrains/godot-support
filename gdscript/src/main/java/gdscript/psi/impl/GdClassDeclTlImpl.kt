package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.stub.GdClassDeclStub
import gdscript.model.GdTutorial
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdInheritance
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getParentName
import gdscript.psi.GdTopLevelDecl
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdCommentUtil.brief
import gdscript.psi.utils.GdCommentUtil.description
import gdscript.psi.utils.GdCommentUtil.isDeprecated
import gdscript.psi.utils.GdCommentUtil.isExperimental
import gdscript.psi.utils.GdCommentUtil.tutorials

class GdClassDeclTlImpl : GdClassDeclElementImpl, GdClassDeclTl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: GdClassDeclStub, type: IStubElementType<*, *>) : super(stub, type)

    fun accept(visitor: GdVisitor) {
        visitor.visitClassDeclTl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val classNameNmi: GdClassNameNmi?
        get() = PsiTreeUtil.getStubChildOfType(this, GdClassNameNmi::class.java)

    override val inheritanceList: List<GdInheritance>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(this, GdInheritance::class.java)

    override val topLevelDeclList: List<GdTopLevelDecl>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdTopLevelDecl::class.java)

    override fun getName(): String = getName(this)

    override val parentName: String
        get() = getParentName(this)

    override fun description(): String =
        description(this)

    override fun brief(): String =
        brief(this)

    override fun tutorials(): List<GdTutorial> =
        tutorials(this)

    override fun isDeprecated(): Boolean =
        isDeprecated(this)

    override fun isExperimental(): Boolean =
        isExperimental(this)
}
