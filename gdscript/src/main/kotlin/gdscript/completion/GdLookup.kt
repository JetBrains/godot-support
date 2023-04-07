package gdscript.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.competion.utils.GdLookupElementBuilder
import gdscript.competion.utils.GdLookupInsertHandler
import java.awt.Color
import javax.swing.Icon

/**
 * Lookup Element factory
 */
object GdLookup {

    const val KEYWORDS = 90.0;
    const val PRIORITY_KEYWORDS = 95.0;
    const val BUILT_IN = 100.0;
    const val USER_DEFINED = 200.0;
    const val REMOTE_DEFINED = 220.0; // resources
    const val LOCAL_USER_DEFINED = 250.0
    const val TOP = 500.0

    // TODO from settings?
    val COLOR_ANNOTATION = Color(255, 179,115);
    val COLOR_GROUP = Color(255, 150,150);
    val COLOR_RESOURCE = Color(99, 194,89);

    fun create(
        text: String,
        lookup: String? = null,
        typed: String? = null,
        priority: Double? = null,
        icon: Icon? = null,
        color: Color? = null,
        presentable: String? = null,
        tail: String? = null,
        handler: InsertHandler<LookupElement>? = null,
    ): LookupElement {
        var builder = GdLookupElementBuilder
            .create(text, if (lookup !== null) lookup else text)
            .withTailText(tail)
            .withTypeText(typed)
            .withIcon(icon)

        if (presentable !== null) {
            builder = builder.withPresentableText(presentable);
        }

        if (handler !== null || lookup != null) {
            builder = builder.withInsertHandler(handler ?: GdLookupInsertHandler.INSTANCE);
        }

        if (color !== null) {
            builder = builder.withItemTextForeground(color);
        }

        if (priority === null) {
            return builder;
        }

        return PrioritizedLookupElement.withPriority(
            builder,
            priority,
        );
    }

}
