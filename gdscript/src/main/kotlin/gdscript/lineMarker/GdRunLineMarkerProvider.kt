package gdscript.lineMarker

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.psi.PsiElement
import gdscript.action.GdRunAction
import gdscript.psi.GdInheritanceIdNmi

class GdRunLineMarkerProvider : RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        if (element.parent !is GdInheritanceIdNmi) {
            return null;
        }

        return Info(GdRunAction(element.parent as GdInheritanceIdNmi));
    }

}
