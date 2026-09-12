package gdscript.utils

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiComment

object GdCustomRegionUtil {

    const val REGION_PREFIX: String = "#region"
    const val END_REGION_PREFIX: String = "#endregion"

    fun isCustomRegionComment(text: String): Boolean =
        getMarkerLength(text) > 0

    fun isCustomFoldingCandidate(node: ASTNode): Boolean {
        if (node.psi !is PsiComment) return false
        return isCustomRegionComment(node.text)
    }

    fun getMarkerLength(text: String): Int = when {
        isMarker(text, REGION_PREFIX) -> REGION_PREFIX.length
        isMarker(text, END_REGION_PREFIX) -> END_REGION_PREFIX.length
        else -> 0
    }

    private fun isMarker(text: String, prefix: String): Boolean {
        if (!text.startsWith(prefix)) return false
        if (text.length == prefix.length) return true
        val nextChar = text[prefix.length]
        return !nextChar.isLetterOrDigit() && nextChar != '_'
    }
}
