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
import gdscript.psi.utils.GdNodeUtil

/**
 * $NodePath & %NodeName read from .tscn
 * Resource as string completion (currently it's not as reference, due to user:// but can be later added) TODO
 */
class GdResourceCompletionContributor : CompletionContributor() {

    val NODE_PATH = psiElement(GdTypes.NODE_PATH_LEX);
    val NODE_PATH_ROOT = NODE_PATH.withSuperParent(3, psiElement(GdFile::class.java));
    val STRING = psiElement(GdTypes.STRING);

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;
        if (GdRefIdCompletionUtil.DIRECT_REF.accepts(position)) {
            // TODO
            //GdResourceCompletionUtil.resources(position.originalElement, result);
            // TODO decide if under class
            GdNodeUtil.listNodes(position).forEach {
                result.addElement(it.lookup())
            }
//            result.addAllElements(GdResourceCompletionUtil.listVarResources(position.originalElement));
        } else if (NODE_PATH_ROOT.accepts(position)) {
            // TODO
//            GdResourceCompletionUtil.fullVarResources(position.originalElement, result);
        } else if (NODE_PATH.accepts(position)) {
            // TODO decide if under class
            result.addAllElements(GdResourceCompletionUtil.listVarResources(position.originalElement));
            // TODO
            //GdResourceCompletionUtil.resources(position.originalElement, result);
        } else if (STRING.accepts(position)) {
            GdFileCompletionUtil.listFileResources(position.project).forEach { result.addElement(it) }
        }
    }

}