package gdscript.psi.manipulator

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.util.PsiUtilCore
import gdscript.psi.GdStringValRef
import gdscript.utils.stringContentRange

/**
 * Changes the content of a string or StringName literal and keeps its prefix and delimiters.
 *
 * A classic rename of a signal or method own-reference reaches the literal through [gdscript.search.GdOwnReferencesSearcher].
 * Without a manipulator, that rename stops with "No ElementManipulator instance registered".
 */
class GdStringValElementManipulator : AbstractElementManipulator<GdStringValRef>() {

    override fun getRangeInElement(element: GdStringValRef): TextRange = stringContentRange(element.text)

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
