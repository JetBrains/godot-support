package gdscript.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import gdscript.GdIcon
import gdscript.GdScriptBundle
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
        return GdScriptPluginIcons.GDScriptIcons.SLOT
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is GdMethodIdNmi) return

        val connections = TscnMethodSearcher(element, element.project).listMethodReferences()
        if (connections.isEmpty()) return

        val nodes = connections.mapNotNull { it.element }

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            GdScriptPluginIcons.GDScriptIcons.SLOT
        )
            .setTargets(nodes)
            .setPopupTitle(GdScriptBundle.message("line.marker.tscn.popup.title"))
            .setTooltipText(GdScriptBundle.message("line.marker.tscn.popup.description"))
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        if (element is TscnConnectionHeader)
                            return GdScriptPluginIcons.GDScriptIcons.SIGNAL
                        else if (element is TscnParagraph)
                            return GdScriptPluginIcons.GDScriptIcons.ANIMATION
                        return GdScriptPluginIcons.GDScriptIcons.NODE
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
