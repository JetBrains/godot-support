package gdscript

import com.intellij.lang.Language

class GdLanguage : Language("GdScript") {

    companion object {
        val INSTANCE: GdLanguage = GdLanguage()
    }

}
