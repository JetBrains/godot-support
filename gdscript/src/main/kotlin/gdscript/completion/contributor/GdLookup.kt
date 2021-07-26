package gdscript.completion.contributor

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import gdscript.competion.utils.GdLookupElementBuilder
import gdscript.competion.utils.GdLookupInsertHandler

object GdLookup {

    fun text(text: String): LookupElement =
        LookupElementBuilder.create(text)

    fun text(text: String, lookup: String): LookupElement =
        GdLookupElementBuilder
            .create(text, lookup)
            .withInsertHandler(GdLookupInsertHandler.INSTANCE)

}
