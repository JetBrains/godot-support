package gdscript.spellchecker

import com.intellij.spellchecker.BundledDictionaryProvider

class GdBundledDictionaryProvider : BundledDictionaryProvider {
    override fun getBundledDictionaries(): Array<String> = arrayOf("gdscript.dic")
}
