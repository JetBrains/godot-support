package gdscript.settings

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.util.xmlb.annotations.Tag

class GdState {

    companion object {
        val OFF = "off"
        val WARN = "warn"
        val ERR = "err"

        fun getLevel(state: String): HighlightSeverity {
            return when (state) {
                WARN -> HighlightSeverity.WEAK_WARNING
                else -> HighlightSeverity.ERROR
            }
        }
    }

    @Tag("hidePrivate")
    var hidePrivate = true

    @Tag("sdkPath")
    var sdkPath: String? = ""

    @Tag("shortTyped")
    var shortTyped = false

    @Tag("annotators")
    var annotators: String = OFF

}
