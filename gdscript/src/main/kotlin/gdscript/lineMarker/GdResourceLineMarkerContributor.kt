package gdscript.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.psi.PsiElement
import gdscript.GdIcon
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.utils.GdInheritanceUtil
import javax.swing.Icon

/**
 * From resource classes marks usage of "res://" references
 */
class GdResourceLineMarkerContributor : RelatedItemLineMarkerProvider() {

    override fun getIcon(): Icon? {
        return GdScriptPluginIcons.GDScriptIcons.RESOURCE
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element.parent !is GdInheritanceIdRef || !GdInheritanceUtil.isExtending(element, "Resource")) return;

        // TODO
//        val usages = GdUserFileIndex.INSTANCE.resourceFiles(
//            element.project,
//            element.containingFile.virtualFile.resourcePath()
//        )
//        if (usages.isEmpty()) return;

//        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
//            GdIcon.getEditorIcon(GdIcon.RESOURCE)
//        )
//            .setTargets(usages.toList())
//            .setPopupTitle("Resource Usage")
//            .setTooltipText("Navigate resource usages")
//            .setCellRenderer {
//                object : PsiElementListCellRenderer<PsiElement>() {
//                    override fun getIcon(element: PsiElement?): Icon {
//                        return GdIcon.getEditorIcon(GdIcon.SLOT)
//                    }
//
//                    override fun getToolTipText(): String? {
//                        return null
//                    }
//
//                    override fun getElementText(element: PsiElement?): String {
//                        if (element == null) return ""
//                        return GdClassMemberUtil.firstNamedDeclarationName(element) ?: element.text
//                    }
//
//                    override fun getContainerText(element: PsiElement?, name: String?): String {
//                        return element?.containingFile?.virtualFile?.localPath() ?: ""
//                    }
//                }
//            }
//
//        result.add(builder.createLineMarkerInfo(element));
    }

}
