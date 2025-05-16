package gdscript

import com.intellij.lang.Language

object GdLanguage : Language("GdScript") {
    override fun getDisplayName(): String = GdScriptBundle.message("language.name")
}
