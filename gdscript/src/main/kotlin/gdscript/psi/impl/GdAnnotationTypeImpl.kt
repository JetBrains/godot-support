package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElementVisitor
import gdscript.polySymbols.GdPolySymbolKind.ANNOTATION
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.psi.GdAnnotationType
import gdscript.psi.GdVisitor
import org.jetbrains.annotations.Unmodifiable

class GdAnnotationTypeImpl(node: ASTNode) : GdRefElementImpl(node), GdAnnotationType {
    fun accept(visitor: GdVisitor) {
        visitor.visitAnnotationType(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    /**
     * The text is `@name`, but the annotation symbol name has no `@`.
     * An unknown annotation gets no reference, because `GdAnnotationAnnotator` already reports it.
     */
    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> {
        val name = text.removePrefix("@")
        if (name.isEmpty() || GdPolySymbolQueriesUtil.getAnnotationSymbol(project, name) == null) return emptyList()
        return polySymbolOwnReferences(this) {
            resolveFromNameMatchQuery(ANNOTATION, name, TextRange(textLength - name.length, textLength))
        }
    }
}
