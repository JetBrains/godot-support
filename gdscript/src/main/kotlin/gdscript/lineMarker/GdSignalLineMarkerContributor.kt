package gdscript.lineMarker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdIcon
import gdscript.index.impl.GdFileResIndex
import gdscript.index.impl.GdSignalDeclIndex
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdSignalDeclTl
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.localPath
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.index.impl.TscnConnectionIndex
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnConnectionHeader
import tscn.psi.TscnNodeHeader
import javax.swing.Icon

/**
 * Signal marker from method to signal declaration
 */
class GdSignalLineMarkerContributor : RelatedItemLineMarkerProvider() {

    override fun getIcon(): Icon? {
        return GdIcon.getEditorIcon(GdIcon.SLOT);
    }

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>,
    ) {
        if (element.parent !is GdMethodIdNmi) return;

        val resource = element.containingFile.virtualFile.resourcePath();
        val sceneFile = TscnResourceIndex.getGlobally(resource, element).firstOrNull() ?: return;

        val connections = TscnConnectionIndex.getInFile(element.text, sceneFile);
        if (connections.isEmpty()) return;

        val nodeMap = hashMapOf<String, TscnNodeHeader>()
        PsiTreeUtil.findChildrenOfType(sceneFile.containingFile, TscnNodeHeader::class.java).forEach {
            nodeMap[it.directParentPath] = it;
        }

        val targets = connections.map {
            val node = nodeMap[it.from];
            val resourcePath = node?.scriptResource ?: return@map it;
            val file = GdFileResIndex.getFiles(resourcePath, element.project).firstOrNull() ?: return@map it;
            val psiFile = file.getPsiFile(element.project) ?: return@map it;
            GdSignalDeclIndex.getInFile(it.signal, psiFile).firstOrNull() ?: it
        }

        val builder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(
            GdIcon.getEditorIcon(GdIcon.SLOT)!!
        )
            .setTargets(targets)
            .setPopupTitle("Signal Connections")
            .setTooltipText("Navigate signal definition")
            .setCellRenderer {
                object : PsiElementListCellRenderer<PsiElement>() {
                    override fun getIcon(element: PsiElement?): Icon {
                        if (element is GdSignalDeclTl)
                            return GdIcon.getEditorIcon(GdIcon.SLOT)!!;

                        return GdIcon.getEditorIcon(GdIcon.SIGNAL)!!;
                    }

                    override fun getToolTipText(): String? {
                        return null;
                    }

                    override fun getElementText(element: PsiElement?): String {
                        if (element is GdSignalDeclTl) return element.name;
                        if (element is TscnConnectionHeader) return element.signal;

                        return element?.text ?: "";
                    }

                    override fun getContainerText(element: PsiElement?, name: String?): String {
                        if (element is TscnConnectionHeader) {
                            val node = nodeMap[element.from];
                            return node?.name ?: element.text;
                        };

                        return element?.containingFile?.virtualFile?.localPath() ?: "";
                    }
                }
            }

        result.add(builder.createLineMarkerInfo(element));
    }

}
