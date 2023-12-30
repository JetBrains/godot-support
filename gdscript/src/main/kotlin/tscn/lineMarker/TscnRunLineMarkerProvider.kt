package tscn.lineMarker

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import tscn.action.TscnRunAction
import tscn.psi.TscnTypes

class TscnRunLineMarkerProvider : RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        if (element.elementType != TscnTypes.GD_SCENE) return null

        return Info(TscnRunAction(element.containingFile))
    }

}
