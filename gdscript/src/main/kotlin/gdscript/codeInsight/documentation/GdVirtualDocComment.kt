package gdscript.codeInsight.documentation

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import gdscript.psi.GdTypes

/**
 * Represents a documentation comment built from a contiguous run of `##` [PsiComment] leaves.
 * Used to support Reader Mode (inline rendered documentation) for GDScript.
 */
class GdVirtualDocComment(val comments: List<PsiComment>) : FakePsiElement(), PsiDocCommentBase {

    override fun getParent(): PsiElement = comments.first().parent

    override fun getTokenType(): IElementType = comments.first().tokenType

    override fun getTextRange(): TextRange = TextRange.create(comments.first().startOffset, comments.last().endOffset)

    override fun getText(): String = comments.joinToString("\n") { it.text }

    override fun delete() {
        comments.forEach { it.delete() }
    }

    /**
     * The element the comment block documents: the first non-whitespace, non-comment, non-annotation
     * sibling following the last comment line.
     */
    override fun getOwner(): PsiElement? {
        var next: PsiElement? = comments.last().nextSibling
        while (next != null) {
            when (next.elementType) {
                TokenType.WHITE_SPACE, GdTypes.COMMENT, GdTypes.ANNOTATION_TL -> next = next.nextSibling
                else -> return next
            }
        }
        return next
    }

    override fun copy(): PsiElement = GdVirtualDocComment(comments)

}
