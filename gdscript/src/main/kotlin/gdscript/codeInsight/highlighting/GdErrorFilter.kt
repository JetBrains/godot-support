package gdscript.codeInsight.highlighting

import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.psi.PsiErrorElement
import gdscript.lsp.GodotLspRunningStatusProvider

class GdErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(el: PsiErrorElement): Boolean {
        if (GodotLspRunningStatusProvider.isLspRunning(el.project))
            return false
        return true
    }
}