package tscn.lineMarker

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import gdscript.utils.isRiderGodotSupportPluginInstalled
import tscn.action.TscnRunAction
import tscn.psi.TscnTypes

class TscnRunLineMarkerProvider : RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        // RIDER-126489 Run configuration in the GdScript plugin
        if (PluginManagerCore.isRiderGodotSupportPluginInstalled())
            return null

        if (element.elementType != TscnTypes.GD_SCENE) return null

        return Info(TscnRunAction(element.containingFile))
    }

}
