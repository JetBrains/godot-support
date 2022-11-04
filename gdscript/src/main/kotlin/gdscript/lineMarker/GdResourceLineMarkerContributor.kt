package gdscript.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import gdscript.GdIcon
import gdscript.psi.GdInheritanceIdNm
import gdscript.psi.GdPreloadNm
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.psi.utils.GdResourceUtil
import gdscript.utils.VirtualFileUtil.localPath
import javax.swing.Icon

/**
 * From resource classes marks usage of "res://" references
 */
class GdResourceLineMarkerContributor : RelatedItemLineMarkerProvider() {

    override fun getIcon(): Icon? {
        return GdIcon.getEditorIcon(GdIcon.RESOURCE);
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element.parent !is GdInheritanceIdNm || !GdInheritanceUtil.isExtending(element, "Resource")) return;

        val usages = GdResourceUtil.findResourcesByName(element);
        if (usages.isEmpty()) return;

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            GdIcon.getEditorIcon(GdIcon.RESOURCE)!!
        )
            .setTargets(*usages)
            .setPopupTitle("Resource Usage")
            .setTooltipText("Navigate resource usages")
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        if (element is GdPreloadNm)
                            return GdIcon.getEditorIcon(GdIcon.SLOT)!!;

                        return GdIcon.getEditorIcon(GdIcon.STRING)!!;
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
