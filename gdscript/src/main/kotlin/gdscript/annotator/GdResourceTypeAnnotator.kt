package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdNodePath
import gdscript.psi.GdTypes
import tscn.psi.TscnNodeHeader
import tscn.psi.utils.TscnScriptUtil

/**
 * Colors named resources
 * Checks for existence
 */
class GdResourceTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdNodePath) {
            resourceExists(element, holder);
        } else if (element.elementType == GdTypes.STRING) {
            stringResourceExists(element, holder);
        }
    }

    private fun stringResourceExists(element: PsiElement, holder: AnnotationHolder) {
        val text = element.text.trim('"');
        if (text.startsWith("res://") && GdFileResIndex.getFiles(text, element.project).isEmpty()) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Resource not found")
                .range(TextRange.create(element.textRange.startOffset + 1, element.textRange.endOffset - 1))
                .create();
        } else if (text.startsWith("%")) {
            // TODO ii kontrolovat %UniqueName - tohle není var a = %node, ale např. get_node("%node")
        }
    }

    private fun resourceExists(element: GdNodePath, holder: AnnotationHolder) {
        val text = element.text;
        val name = text.substring(1);
        val scene = TscnScriptUtil.getSceneFile(element);

        if (scene != null) {
            val nodes = PsiTreeUtil.getStubChildrenOfTypeAsList(scene.containingFile, TscnNodeHeader::class.java);
            if (text.startsWith('%')) {
                nodes.forEach {
                    if (it.name == name && it.isUniqueNameOwner) {
                        return;
                    }
                }
            } else {
                nodes.forEach {
                    if (it.nodePath == name) {
                        return;
                    }
                }
            }
        }

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Node not found")
            .range(element.textRange)
            .create();
    }

}
