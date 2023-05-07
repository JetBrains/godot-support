package gdscript.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import gdscript.GdIcon
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.localPath
import javax.swing.Icon

class GdTraitLineMarkerContributor : RelatedItemLineMarkerProvider() {

    companion object {
        val PREFIX = "# Trait ";
        val SUFFIX = "# EndTrait";
    }

    override fun getIcon(): Icon? {
        return GdIcon.getEditorIcon(GdIcon.LINK);
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element !is PsiComment) return;
        val text = element.text;
        if (!text.startsWith(PREFIX)) return;
        val file = text.substring(PREFIX.length).trim();
        val project = element.project;

        val definitons = GdFileResIndex.INSTANCE.getFiles("res://$file", project)
            .mapNotNull { it.getPsiFile(project)?.firstChild };

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            GdIcon.getEditorIcon(if (definitons.isEmpty()) GdIcon.ERROR else GdIcon.LINK)!!
        )
            .setTargets(definitons)
            .setPopupTitle("Trait")
            .setTooltipText(if (definitons.isEmpty()) "Missing Trait file" else "Trait source file")
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        return GdIcon.getEditorIcon(GdIcon.LINK)!!;
                    }

                    override fun getToolTipText(): String? {
                        return null;
                    }

                    override fun getElementText(element: PsiElement?): String {
                        if (element == null) return "";
                        return GdClassMemberUtil.firstNamedDeclarationName(element) ?: element.text;
                    }

                    override fun getContainerText(element: PsiElement?, name: String?): String {
                        return element?.containingFile?.virtualFile?.localPath() ?: "";
                    }
                }
            }

        result.add(builder.createLineMarkerInfo(element));
    }

}
