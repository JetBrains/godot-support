package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElementVisitor
import gdscript.polySymbols.GdPolySymbolKind.INHERITANCE_SYMBOLS
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.quotedContentRange
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdPsiUtils.isClassName
import gdscript.psi.GdVisitor
import org.jetbrains.annotations.Unmodifiable

class GdInheritanceIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdInheritanceIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitInheritanceIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val isClassName: Boolean
        get() = isClassName(this)

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> {
        // Resource-path inheritance (`extends "res://base.gd"`) keeps the surrounding quotes as
        // part of this element's own text; the queried symbol's name never includes them (see
        // GdPsiResourceClassSymbol.name/GdPsiClassSymbol.name), so the quotes must be excluded here
        // to make the queried name/range match. If the resolved class has a class_name (its own
        // name differs from the resource path), GdPsiResourceClassesPolySymbolScope wraps it in a
        // name-aliasing delegate reporting the resource-path name, so the own-reference's
        // name-equals-text requirement still holds either way.
        val range = quotedContentRange(text)
        val name = range.substring(text)
        return polySymbolOwnReferences(this) {
            resolveFromNameMatchQuery(INHERITANCE_SYMBOLS, name, range)
        }
    }
}
