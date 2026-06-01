package gdscript.spellchecker

import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.spellchecker.inspections.SpellCheckingInspection
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.Tokenizer
import gdscript.psi.GdTypes

class GdSpellcheckingStrategy : SpellcheckingStrategy(), DumbAware {
    override fun getTokenizer(element: PsiElement, scope: Set<SpellCheckingInspection.SpellCheckingScope?>): Tokenizer<*> {
        if (element is LeafPsiElement && element.elementType == GdTypes.STRING) {
            if (isGodotResourcePath(element.text)) return EMPTY_TOKENIZER
            return TEXT_TOKENIZER
        }
        return super.getTokenizer(element)
    }

    private fun isGodotResourcePath(text: String): Boolean {
        return text.startsWith("res://", 1) || text.startsWith("uid://", 1)
    }
}
