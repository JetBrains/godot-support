package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.polySymbols.references.PolySymbolOwnReferences
import com.intellij.polySymbols.references.PolySymbolOwnReferencesHost
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import gdscript.polySymbols.GdPolySymbolKind.INHERITANCE_SYMBOLS
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdPsiUtils.getPsiFile
import gdscript.psi.GdPsiUtils.isClassName
import gdscript.psi.GdVisitor

class GdInheritanceIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdInheritanceIdRef, PolySymbolOwnReferencesHost {
    fun accept(visitor: GdVisitor) {
        visitor.visitInheritanceIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val psiFile: PsiFile?
        get() = getPsiFile(this)

    override val isClassName: Boolean
        get() = isClassName(this)

    override fun buildOwnReferences(builder: PolySymbolOwnReferences.Builder) {
        builder.fromNameMatchQuery(INHERITANCE_SYMBOLS, text)
    }
}
