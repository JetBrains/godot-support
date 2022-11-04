package gdscript.lineMarker

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.psi.PsiElement
import gdscript.action.GdRunAction
import gdscript.psi.GdInheritanceIdNm

class GdRunLineMarkerProvider : RunLineMarkerContributor() {

    // TODO nemůže být na inner class
    override fun getInfo(element: PsiElement): Info? {
        // TODO pouze tam, kde je připojen script ke scéně
        return null;
        if (element.parent !is GdInheritanceIdNm) {
            return null;
        }

        return Info(GdRunAction(element.parent as GdInheritanceIdNm));
    }

}
