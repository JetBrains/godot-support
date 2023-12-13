package gdscript.settings

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.util.xmlb.annotations.Tag

class GdProjectState {

    companion object {
        val OFF = "off"
        val WARN = "warn"
        val ERR = "err"

        fun selectedLevel(state: String): HighlightSeverity {
            return when (state) {
                WARN -> HighlightSeverity.WEAK_WARNING
                else -> HighlightSeverity.ERROR
            }
        }

        fun shouldWarnInspection(warn: Boolean): ProblemHighlightType {
            return if (warn) ProblemHighlightType.WEAK_WARNING else ProblemHighlightType.INFORMATION
        }
    }

    @Tag("hidePrivate")
    var hidePrivate = true

    @Tag("sdkPath")
    var sdkPath: String? = ""

    @Tag("shouldType")
    var shouldType = false

    @Tag("shortTyped")
    var shortTyped = false

    @Tag("annotators")
    var annotators: String = OFF

    @Tag("criticals")
    var criticals: String = "ALERT,ATTENTION,CAUTION,CRITICAL,DANGER,SECURITY"

    @Tag("warnings")
    var warnings: String = "BUG,DEPRECATED,FIXME,HACK,TASK,TBD,TODO,WARNING"

    @Tag("notes")
    var notes: String = "INFO,NOTE,NOTICE,TEST,TESTING"

}
