package tscn.psi.manipulator

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.util.PsiUtilCore
import gdscript.utils.PsiTraceUtil.describeForTrace
import gdscript.utils.PsiTraceUtil.forTrace

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
        thisLogger().trace {
            "handleContentChange: element=${element.describeForTrace()}, range=$range, newContent=$newContent"
        }

        val offset = element.textRange.startOffset + range.startOffset
        val leaf = PsiUtilCore.getElementAtOffset(element.containingFile, offset)
        if (leaf === element.containingFile) {
            thisLogger().trace { "handleContentChange: no leaf at ${range.startOffset}, change dropped" }
            return null
        }
        val node = leaf.node as? LeafElement
        if (node == null) {
            thisLogger().trace { "handleContentChange: ${leaf.describeForTrace()} is not a leaf, change dropped" }
            return null
        }
        val rangeInLeaf = range.shiftLeft(leaf.textRange.startOffset - element.textRange.startOffset)
        if (rangeInLeaf.startOffset < 0 || rangeInLeaf.endOffset > leaf.textLength) {
            thisLogger().trace {
                "handleContentChange: $rangeInLeaf is outside ${leaf.describeForTrace()}, change dropped"
            }
            return null
        }

        val newText = rangeInLeaf.replace(leaf.text, newContent)
        node.replaceWithText(newText)
        thisLogger().trace {
            "handleContentChange: wrote '${newText.forTrace()}', element is now ${element.describeForTrace()}"
        }
        return element
    }
}
