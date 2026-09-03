package tscn.psi.manipulator

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafElement

/**
 * Writes a new name into a part of a scene element, for example the `method` value of a connection
 * header or the `"method"` key of an animation track.
 *
 * A classic rename asks the manipulator of the host element to change the text under a
 * [com.intellij.psi.PsiReference] - see [com.intellij.psi.PsiReferenceBase.handleElementRename]. A
 * rename of a GDScript declaration reaches a scene element through
 * [gdscript.search.GdOwnReferencesSearcher], which bridges an own reference into a classic
 * reference, so the scene needs a manipulator. Without one the rename stops with "No
 * ElementManipulator instance registered".
 *
 * The change keeps the text around the range, so the quotes of a value and the `&` of a
 * [tscn.psi.TscnJsonValue] stay as they are.
 */
class TscnElementManipulator : AbstractElementManipulator<PsiElement>() {

    override fun handleContentChange(element: PsiElement, range: TextRange, newContent: String): PsiElement? {
        val leaf = element.findElementAt(range.startOffset) ?: return null
        val node = leaf.node as? LeafElement ?: return null
        val rangeInLeaf = range.shiftLeft(leaf.textRange.startOffset - element.textRange.startOffset)
        if (rangeInLeaf.startOffset < 0 || rangeInLeaf.endOffset > leaf.textLength) return null

        node.replaceWithText(rangeInLeaf.replace(leaf.text, newContent))
        return element
    }
}
