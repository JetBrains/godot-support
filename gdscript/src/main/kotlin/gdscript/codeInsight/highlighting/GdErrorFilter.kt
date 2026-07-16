package gdscript.codeInsight.highlighting

import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.psi.PsiErrorElement
import gdscript.lsp.GodotLspRunningStatusProvider
import gdscript.psi.GdFile

class GdErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(el: PsiErrorElement): Boolean {
        if (el.containingFile is GdFile && GodotLspRunningStatusProvider.isLspRunning(el.project))
            return false
        return true
    }
}