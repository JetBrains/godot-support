package gdscript.lineMarker

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.action.GdRunAction
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.utils.isRiderGodotSupportPluginInstalled

/**
 * Run current scene LineMarker
 */
class GdRunLineMarkerProvider : RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        // RIDER-126489 Run configuration in the GdScript plugin
        if (PluginManagerCore.isRiderGodotSupportPluginInstalled())
            return null
        if (element.parent !is GdInheritanceIdRef || GdInheritanceUtil.isExtending(element, "Resource"))
            return null
        if (GdInheritanceUtil.getExtendedElement(element) !is PsiFile) return null

        return Info(GdRunAction(element.parent as GdInheritanceIdRef))
    }

}
