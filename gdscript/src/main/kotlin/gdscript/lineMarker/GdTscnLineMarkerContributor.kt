package gdscript.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import gdscript.GdIcon
import gdscript.psi.GdMethodIdNmi
import gdscript.utils.VirtualFileUtil.localPath
import tscn.psi.TscnConnectionHeader
import tscn.psi.TscnParagraph
import tscn.psi.search.TscnMethodSearcher
import javax.swing.Icon

/**
 * Signal marker from method to signal declaration
 */
class GdTscnLineMarkerContributor : RelatedItemLineMarkerProvider() {

    override fun getIcon(): Icon? {
        return GdIcon.getEditorIcon(GdIcon.SLOT);
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is GdMethodIdNmi) return;

        val connections = TscnMethodSearcher(element, element.project).listMethodReferences()
        if (connections.isEmpty()) return

        val nodes = connections.mapNotNull { it.element }

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            GdIcon.getEditorIcon(GdIcon.SLOT)
        )
            .setTargets(nodes)
            .setPopupTitle("Tscn Connections")
            .setTooltipText("Navigate Tscn definitions")
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        if (element is TscnConnectionHeader)
                            return GdIcon.getEditorIcon(GdIcon.SIGNAL)
                        else if (element is TscnParagraph)
                            return GdIcon.getEditorIcon(GdIcon.ANIMATION)
                        return GdIcon.getEditorIcon(GdIcon.NODE)
                    }

                    override fun getElementText(element: PsiElement?): String {
                        if (element is TscnConnectionHeader) return element.signal
                        else if (element is TscnParagraph) return "Animation"

                        return element?.text ?: ""
                    }

                    override fun getContainerText(element: PsiElement?, name: String?): String {
                        return element?.containingFile?.virtualFile?.localPath() ?: ""
                    }
                }
            }

        result.add(builder.createLineMarkerInfo(element.firstChild))
    }

}
