package gdscript.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiElement
import gdscript.GdIcon
import javax.swing.Icon

class GdSignalLineMarkerContributor : RelatedItemLineMarkerProvider() {

//    override fun getIcon(): Icon? {
//        GdIcon.SIGNAL_MARKER;
//    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        super.collectNavigationMarkers(element, result)
    }

}
