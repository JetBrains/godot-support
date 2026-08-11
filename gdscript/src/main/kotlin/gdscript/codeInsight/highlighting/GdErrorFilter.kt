package gdscript.codeInsight.highlighting

import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.psi.PsiErrorElement
import gdscript.lsp.GodotLspRunningStatusProvider
import gdscript.psi.GdFile
import gdscript.psi.GdPsiCodeFragment

class GdErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(el: PsiErrorElement): Boolean {
        if (el.containingFile is GdPsiCodeFragment) return true // Code fragments don't use LSP, so we need our annotations
        if (el.containingFile is GdFile && GodotLspRunningStatusProvider.isLspRunning(el.project))
            return false
        return true
    }
}