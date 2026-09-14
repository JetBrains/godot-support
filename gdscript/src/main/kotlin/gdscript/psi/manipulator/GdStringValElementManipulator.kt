package gdscript.psi.manipulator

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.util.PsiUtilCore
import gdscript.psi.GdStringValRef

/**
 * Writes a new name into a quoted string or a StringName literal that holds a signal/method
 * own-reference.
 *
 * A classic rename reaches the host through [gdscript.search.GdOwnReferencesSearcher], which
 * bridges an own-reference into a classic [com.intellij.psi.PsiReference]. Without a manipulator the
 * rename stops with "No ElementManipulator instance registered" - the same reason
 * [tscn.psi.manipulator.TscnElementManipulator] exists for scene values. The change keeps the
 * quotes and the `&` prefix of a StringName.
 */
class GdStringValElementManipulator : AbstractElementManipulator<GdStringValRef>() {

    override fun handleContentChange(element: GdStringValRef, range: TextRange, newContent: String): GdStringValRef? {
        val offset = element.textRange.startOffset + range.startOffset
        val leaf = PsiUtilCore.getElementAtOffset(element.containingFile, offset)
        if (leaf === element.containingFile) return null
        val node = leaf.node as? LeafElement ?: return null
        val rangeInLeaf = range.shiftLeft(leaf.textRange.startOffset - element.textRange.startOffset)
        if (rangeInLeaf.startOffset < 0 || rangeInLeaf.endOffset > leaf.textLength) return null

        node.replaceWithText(rangeInLeaf.replace(leaf.text, newContent))
        return element
    }
}
