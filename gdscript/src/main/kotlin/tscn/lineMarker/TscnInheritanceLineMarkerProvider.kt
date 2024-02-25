package tscn.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.firstLeaf
import gdscript.GdIcon
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.VirtualFileUtil.getPsiFile
import tscn.psi.TscnResourceHeader
import javax.swing.Icon

class TscnInheritanceLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun getIcon(): Icon {
        return GdIcon.getEditorIcon(GdIcon.OVERRIDE)
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is TscnResourceHeader) return
        if (element.type != "PackedScene") return
        val path = element.path.trim('"')
        val project = element.project
        val target = GdFileResIndex.INSTANCE.getFiles(path, project).firstOrNull() ?: return

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            GdIcon.getEditorIcon(GdIcon.OVERRIDE)
        )
            .setTargets(target.getPsiFile(project))
            .setPopupTitle("Inherited Scene")
            .setTooltipText("Navigate to inherited scene")
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        return GdIcon.getEditorIcon(GdIcon.OVERRIDE)
                    }

                    override fun getElementText(element: PsiElement?): String {
                        return path
                    }

                    override fun getContainerText(element: PsiElement?, name: String?): String {
                        return path
                    }
                }
            }

        result.add(builder.createLineMarkerInfo(element.firstLeaf))
    }


}
