package gdscript.completion

import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import gdscript.competion.utils.GdLookupElementBuilder
import gdscript.competion.utils.GdLookupInsertHandler
import javax.swing.Icon

object GdLookup {

    const val STATIC = 50.0;
    const val KEYWORDS = 90.0;
    const val PRIORITY_KEYWORDS = 95.0;
    const val USER_DEFINED = 200.0;
    const val STATIC_TOP = 500.0;

    fun create(
        text: String,
        lookup: String? = null,
        typed: String? = null,
        priority: Double? = null,
        icon: Icon? = null
    ): LookupElement {
        val builder = GdLookupElementBuilder
            .create(text, if (lookup !== null) lookup else text)
            .withTypeText(typed)
            .withIcon(icon)

        if (priority === null) {
            return builder
        }

        return PrioritizedLookupElement.withPriority(
            builder,
            priority
        );
    }

}
