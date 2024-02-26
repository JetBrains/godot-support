package tscn.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.firstLeaf
import com.intellij.psi.util.parentOfType
import gdscript.GdIcon
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.VirtualFileUtil.getPsiFile
import tscn.psi.TscnFile
import tscn.psi.TscnNodeHeader
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
        if (!isInheritedScene(element)) return

        val project = element.project
        val target = GdFileResIndex.INSTANCE.getFiles(element.path, project).firstOrNull() ?: return

        val builder = NavigationGutterIconBuilder.create(GdIcon.getEditorIcon(GdIcon.OVERRIDE))
            .setTargets(target.getPsiFile(project))
            .setPopupTitle("Inherited Scene")
            .setTooltipText("Navigate to inherited scene")

        result.add(builder.createLineMarkerInfo(element.firstLeaf))
    }

    private fun isInheritedScene(element: TscnResourceHeader) : Boolean {
        val node = element.parentOfType<TscnFile>()?.descendantsOfType<TscnNodeHeader>()
                ?.firstOrNull { it.instanceResource == element.path }
        // Inherited scene resource is only ever attached to the root resource
        return node?.parentPath?.isEmpty() ?: false
    }

}
