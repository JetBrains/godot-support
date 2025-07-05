package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.prevLeaf
import gdscript.psi.GdArgList
import gdscript.psi.GdCallEx
import gdscript.psi.GdTypes
import gdscript.utils.ElementTypeUtil.isSkipable
import project.psi.model.GdAutoload

object PsiElementUtil {

    private val SKIPS_TO_COMMENT = listOf(TokenType.WHITE_SPACE, GdTypes.INDENT, GdTypes.DEDENT);

    fun PsiElement.precedingNewLines(position: Int): Int {
        val parent = this.parent ?: return 0

        if (parent.text.isEmpty()) return 0
        val partial = parent.containingFile.text.substring(0, position)

        var c = 0
        for (ch in partial.toCharArray().reversed()) {
            if (ch == '\n') {
                c += 1
            } else if (ch != ' ' && ch != '\t') {
                break
            }
        }

        return c
    }

    fun PsiElement.getCaretOffsetIfSingle(): Int? {
        val editor = PsiEditorUtil.findEditor(this) ?: return null
        if (editor.caretModel.caretCount != 1) {
            return null
        }

        return editor.caretModel.currentCaret.offset
    }

    fun PsiElement.getCallExpr(): GdCallEx? {
        val next = PsiTreeUtil.nextVisibleLeaf(this) ?: return null
        if (next.elementType == GdTypes.LRBR && next.parent?.elementType == GdTypes.CALL_EX) {
            return next.parent as GdCallEx
        }

        return null
    }

    fun PsiElement.getCallExprOfParam(): GdCallEx? {
        val argList = PsiTreeUtil.getParentOfType(this, GdArgList::class.java) ?: return null
        if (argList.parent is GdCallEx) return argList.parent as GdCallEx

        return null
    }

    fun PsiElement.nextNonWhiteCommentToken(): PsiElement? {
        var next = this.nextSibling
        while (next != null && next.elementType.isSkipable()) {
            next = next.nextSibling
        }

        return next
    }

    fun PsiElement.prevNonWhiteCommentToken(vararg skip: IElementType): PsiElement? {
        var prev = this.prevSibling
        while (prev != null && (prev.elementType.isSkipable() || skip.contains(prev.elementType))) {
            prev = prev.prevSibling
        }

        return prev
    }

    fun PsiElement.prevCommentBlock(): PsiElement? {
        var prev = this.prevLeaf()
        while (SKIPS_TO_COMMENT.contains(prev?.elementType)) {
            if (prev!!.text == "\n") {
                prev = prev.prevLeaf()
                break
            }
            prev = prev.prevLeaf()
        }

        if (prev?.elementType == GdTypes.COMMENT) return prev
        return null
    }

    fun Any.psi(): PsiElement {
        return when (this) {
            is GdAutoload -> this.element
            else -> this as PsiElement
        }
    }

}
