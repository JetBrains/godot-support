package gdscript.utils

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import gdscript.utils.EditorSettingsUtil.normalIndent

object CompletionParametersUtil {

    fun CompletionParameters.indent(): String {
        val editor = this.editor

        return editor.settings.normalIndent(editor.project!!)
    }

    fun CompletionResultSet.pureMatcher(parameters: CompletionParameters): CompletionResultSet {
        return this.withPrefixMatcher(
            CamelHumpMatcher(
                parameters.position.text.substring(0, parameters.offset - parameters.position.textRange.startOffset),
                true,
            )
        )
    }

}
