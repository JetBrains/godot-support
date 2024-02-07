package gdscript.utils

import com.intellij.codeInsight.completion.CompletionParameters
import gdscript.utils.EditorSettingsUtil.normalIndent

object CompletionParametersUtil {

    fun CompletionParameters.indent(): String {
        val editor = this.editor

        return editor.settings.normalIndent(editor.project!!)
    }

}
