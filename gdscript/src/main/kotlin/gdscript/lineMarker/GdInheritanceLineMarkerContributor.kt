package gdscript.lineMarker

import GdScriptPluginIcons
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import gdscript.GdScriptBundle
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.utils.VirtualFileUtil.localPath
import javax.swing.Icon

/**
 * To super method
 */
class GdInheritanceLineMarkerContributor : RelatedItemLineMarkerProvider() {

    override fun getIcon(): Icon? {
        return GdScriptPluginIcons.GDScriptIcons.OVERRIDE
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is GdMethodIdNmi) return

        val parent = GdInheritanceUtil.getExtendedElement(element)
        val results = mutableListOf<Any>()
        val superMethod = GdClassMemberUtil.collectFromParents(parent, results, element.project, null, element.name) ?: return

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            GdScriptPluginIcons.GDScriptIcons.OVERRIDE
        )
            .setTargets(superMethod)
            .setPopupTitle(GdScriptBundle.message("line.marker.super.method.popup.title"))
            .setTooltipText(GdScriptBundle.message("line.marker.super.method.popup.description"))
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        return GdScriptPluginIcons.GDScriptIcons.OVERRIDE
                    }

                    override fun getToolTipText(): String? {
                        return null
                    }

                    override fun getElementText(element: PsiElement?): String {
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
