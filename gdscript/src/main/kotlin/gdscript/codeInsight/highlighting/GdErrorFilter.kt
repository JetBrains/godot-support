package gdscript.codeInsight.highlighting

import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.psi.PsiErrorElement
import gdscript.annotator.isGodotSupportInstalled
import gdscript.utils.RiderGodotSupportPluginUtil

class GdErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(el: PsiErrorElement): Boolean {
        if (isGodotSupportInstalled && RiderGodotSupportPluginUtil.isGodotSupportLSPRunning(el.project))
            return false
        return true
    }
}