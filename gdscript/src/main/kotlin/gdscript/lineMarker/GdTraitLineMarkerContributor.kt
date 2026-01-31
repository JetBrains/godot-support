package gdscript.lineMarker

import GdScriptPluginIcons
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import gdscript.GdScriptBundle
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.localPath
import javax.swing.Icon

class GdTraitLineMarkerContributor : RelatedItemLineMarkerProvider() {

    companion object {
        val PREFIX = "#trait "
        val SUFFIX = "#endTrait"
    }

    override fun getIcon(): Icon {
        return GdScriptPluginIcons.GDScriptIcons.LINK
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is PsiComment) return
        val text = element.text
        if (!text.startsWith(PREFIX)) return
        val file = text.substring(PREFIX.length).trim()
        val project = element.project

        val definitons = GdFileResIndex.getFiles("res://$file", project)
            .mapNotNull { it.getPsiFile(project)?.firstChild }

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            if (definitons.isEmpty()) GdScriptPluginIcons.GDScriptIcons.ERROR else GdScriptPluginIcons.GDScriptIcons.LINK
        )
            .setTargets(definitons)
            .setPopupTitle(GdScriptBundle.message("line.marker.trait.popup.title"))
            .setTooltipText(
                if (definitons.isEmpty()) GdScriptBundle.message("line.marker.trait.popup.missing.trait.file") else GdScriptBundle.message(
                    "line.marker.trait.popup.missing.source.file"
                )
            )
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        return GdScriptPluginIcons.GDScriptIcons.LINK
                    }

                    override fun getToolTipText(): String? {
                        return null
                    }

                    override fun getElementText(element: PsiElement?): String {
                        if (element == null) return ""
                        return GdClassMemberUtil.firstNamedDeclarationName(element) ?: element.text
                    }

                    override fun getContainerText(element: PsiElement?, name: String?): String {
                        return element?.containingFile?.virtualFile?.localPath() ?: ""
                    }
                }
            }

        result.add(builder.createLineMarkerInfo(element))
    }

}
