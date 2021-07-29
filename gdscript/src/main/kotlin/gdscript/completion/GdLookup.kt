package gdscript.completion

import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import gdscript.competion.utils.GdLookupElementBuilder
import gdscript.competion.utils.GdLookupInsertHandler
import gdscript.highlighter.GdHighlighterColors
import java.awt.Color
import javax.swing.Icon

object GdLookup {

    const val KEYWORDS = 90.0;
    const val PRIORITY_KEYWORDS = 95.0;
    const val USER_DEFINED = 200.0;
    const val STATIC_TOP = 500.0;

    val ANNOTATOR_COLOR = Color(255, 179,115);

    fun create(
        text: String,
        lookup: String? = null,
        typed: String? = null,
        priority: Double? = null,
        icon: Icon? = null,
        color: Color? = null
    ): LookupElement {
        var builder = GdLookupElementBuilder
            .create(text, if (lookup !== null) lookup else text)
            .withTypeText(typed)
            .withIcon(icon)

        if (lookup !== null) {
            builder = builder.withInsertHandler(GdLookupInsertHandler.INSTANCE);
        }

        if (color !== null) {
            builder = builder.withItemTextForeground(color);
        }

        if (priority === null) {
            return builder
        }

        return PrioritizedLookupElement.withPriority(
            builder,
            priority
        );
    }

}
