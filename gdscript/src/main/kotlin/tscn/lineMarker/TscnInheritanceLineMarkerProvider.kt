package tscn.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.firstLeaf
import com.intellij.psi.util.parentOfType
import gdscript.GdIcon
import gdscript.GdScriptBundle
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.localPath
import tscn.psi.TscnFile
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnResourceHeader
import javax.swing.Icon

class TscnInheritanceLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun getIcon(): Icon {
        return GdScriptPluginIcons.GDScriptIcons.OVERRIDE
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is TscnResourceHeader) return
        if (element.type != "PackedScene") return
        if (!isInheritedScene(element)) return

        val project = element.project
        val target = GdFileResIndex.getFiles(element.path, project).firstOrNull() ?: return

        val builder = NavigationGutterIconBuilder.create(GdScriptPluginIcons.GDScriptIcons.OVERRIDE)
            .setTargets(target.getPsiFile(project))
            .setPopupTitle(GdScriptBundle.message("popup.title.inherited.scene"))
            .setTooltipText(GdScriptBundle.message("tooltip.navigate.to.inherited.scene"))
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {

                    override fun getElementText(element: PsiElement?): String {
                        return "Inherited scene"
                    }

                    override fun getContainerText(element: PsiElement?, name: String?): String {
                        return target.localPath() ?: ""
                    }
                }
            }

        result.add(builder.createLineMarkerInfo(element.firstLeaf))
    }

    private fun isInheritedScene(element: TscnResourceHeader) : Boolean {
        val node = element.parentOfType<TscnFile>()?.descendantsOfType<TscnNodeHeader>()
                ?.firstOrNull { it.instanceResource == element.path }
        // Inherited scene resource is only ever attached to the root resource
        return node?.parentPath?.isEmpty() ?: false
    }

}
