package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.completion.utils.GdFileCompletionUtil
import gdscript.completion.utils.GdRefIdCompletionUtil
import gdscript.completion.utils.GdResourceCompletionUtil
import gdscript.psi.GdFile
import gdscript.psi.GdTypes

// TODO tohle rozdělit - resource_string na referenci a zbytek přejmenovat na node_path
class GdResourceCompletionContributor : CompletionContributor() {

    val NODE_PATH = psiElement(GdTypes.NODE_PATH_LEX);
    val NODE_PATH_ROOT = NODE_PATH.withSuperParent(3, psiElement(GdFile::class.java));
    val RESOURCE_STRING = psiElement(GdTypes.STRING);

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;
        if (GdRefIdCompletionUtil.DIRECT_REF.accepts(position)) {
            GdResourceCompletionUtil.resources(position.originalElement, result);
        } else if (NODE_PATH_ROOT.accepts(position)) {
            GdResourceCompletionUtil.fullVarResources(position.originalElement, result);
        } else if (NODE_PATH.accepts(position)) {
            GdResourceCompletionUtil.resources(position.originalElement, result);
        } else if (RESOURCE_STRING.accepts(position)) {
            GdFileCompletionUtil.listFileResources(position.project, false, false).forEach {
                result.addElement(it)
            }
        }
    }

}