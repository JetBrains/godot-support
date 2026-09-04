package gdscript.utils

import com.intellij.psi.PsiElement

/**
 * Helpers that describe PSI in one line for a trace record.
 *
 * Turn the records on with the `Godot` trace scenario - see
 * `com.jetbrains.rider.plugins.godot.logs.GodotLogTraceScenarios`.
 */
object PsiTraceUtil {

    private const val TEXT_LIMIT = 60

    /** The type, the file, the range and the text of [this]. */
    fun PsiElement?.describeForTrace(): String {
        if (this == null) return "null"
        if (!isValid) return "${javaClass.simpleName}(invalid)"
        val file = containingFile?.name ?: "<no file>"
        return "${javaClass.simpleName}@$file$textRange '${text.forTrace()}'"
    }

    /** [this] cut to one short line. */
    fun String?.forTrace(): String {
        if (this == null) return "null"
        val oneLine = replace("\n", "\\n")
        return if (oneLine.length <= TEXT_LIMIT) oneLine else oneLine.take(TEXT_LIMIT) + "..."
    }
}
