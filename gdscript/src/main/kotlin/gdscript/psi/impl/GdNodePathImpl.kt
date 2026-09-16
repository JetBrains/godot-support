package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElementVisitor
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiNodeSymbol
import gdscript.psi.GdNodePath
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdNodeUtil
import org.jetbrains.annotations.Unmodifiable

class GdNodePathImpl(node: ASTNode) : GdRefElementImpl(node), GdNodePath {
    fun accept(visitor: GdVisitor) {
        visitor.visitNodePath(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> {
        val node = GdNodeUtil.findNode(this)?.element ?: return emptyList()
        // A node path's own reference range must be the substring that spells the resolved node's
        // own name - the platform checks that a reference's range text matches its symbol's name.
        // $Node/%UniqueNode always ends in the target's own name, quoted or not, so the last
        // occurrence of that name in the whole path text is that trailing segment.
        val nameIndex = text.lastIndexOf(node.name)
        if (nameIndex < 0) return emptyList()
        val range = TextRange(nameIndex, nameIndex + node.name.length)

        return polySymbolOwnReferences(this) {
            reference(range, GdPolySymbolKind.NODE) { listOf(GdPsiNodeSymbol(node)) }
        }
    }
}
