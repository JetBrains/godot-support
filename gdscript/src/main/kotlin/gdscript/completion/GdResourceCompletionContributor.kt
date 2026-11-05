package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.completion.utils.GdRefIdCompletionUtil
import gdscript.completion.utils.GdStringCompletionUtil
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdFile
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdNodeUtil

/**
 * $NodePath & %NodeName read from .tscn
 * Resource as string completion (currently it's not as reference, due to user:// but can be later added)
 */
class GdResourceCompletionContributor : CompletionContributor() {

    val NODE_PATH = psiElement(GdTypes.NODE_PATH_LEX)
    val NODE_PATH_ROOT = NODE_PATH.withSuperParent(3, psiElement(GdFile::class.java))
    val STRING = psiElement(GdTypes.STRING)
    val STRING_NAME = psiElement(GdTypes.STRING_NAME)

    /** Opening of the quoted unique name form, `%"Name"`. */
    private val QUOTED_UNIQUE_PREFIX = "%\""

    private fun isQuotedUniqueNodePath(parameters: CompletionParameters): Boolean {
        val position = parameters.position
        if (!NODE_PATH.accepts(position)) return false
        if (!position.text.startsWith(QUOTED_UNIQUE_PREFIX)) return false

        return parameters.offset >= position.textRange.startOffset + QUOTED_UNIQUE_PREFIX.length
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val r = result.withPrefixMatcher(CamelHumpMatcher(
            parameters.position.text.substring(0, parameters.offset - parameters.position.textRange.startOffset),
            true,
        ))

        val position = parameters.position
        if (isQuotedUniqueNodePath(parameters)) {
            GdNodeUtil.listNodes(position).forEach { r.addAllElements(it.quotedUniqueLookups()) }
        } else if (GdRefIdCompletionUtil.DIRECT_REF.accepts(position)) {
            GdNodeUtil.listNodes(position).forEach { r.addAllElements(it.lookups()) }
        } else if (NODE_PATH_ROOT.accepts(position)) {
            GdNodeUtil.listNodes(position).forEach { r.addAllElements(it.variableLookups()) }
        } else if (NODE_PATH.accepts(position)) {
            if (GdRefIdCompletionUtil.CLASS_ROOT.accepts(position)) {
                GdNodeUtil.listNodes(position).forEach { r.addAllElements(it.variableLookups()) }
            } else {
                GdNodeUtil.listNodes(position).forEach { r.addAllElements(it.lookups()) }
            }
        } else if (STRING.accepts(position)) {
            val context = GdStringCompletionUtil.inputContext(position)
            GdStringCompletionUtil.addInputs(position, result, context)
            when (context) {
                GdStringCompletionUtil.InputContext.ACTION_ARGUMENT -> return
                GdStringCompletionUtil.InputContext.UNRESOLVED_NAME_MATCH,
                GdStringCompletionUtil.InputContext.OTHER -> {
                    GdFileResIndex.getNonEmptyKeys(position).forEach {
                        result.addElement(
                            GdLookup.create(
                                it,
                                priority = GdLookup.REMOTE_DEFINED,
                            )
                        )
                    }
                    GdStringCompletionUtil.addGroups(position, result)
                    GdStringCompletionUtil.addMetas(position, result)
                }
            }
        } else if (STRING_NAME.accepts(position)) {
            val context = GdStringCompletionUtil.inputContext(position)
            when (context) {
                GdStringCompletionUtil.InputContext.ACTION_ARGUMENT,
                GdStringCompletionUtil.InputContext.UNRESOLVED_NAME_MATCH -> {
                    GdStringCompletionUtil.addInputs(position, result, context)
                }

                // TODO: Add group and metadata completion in StringName literals in a follow-up ticket.
                // Node.add_to_group and Object.get_meta take StringName parameters.
                GdStringCompletionUtil.InputContext.OTHER -> Unit
            }
        }
    }

}
