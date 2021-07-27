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

    // text + lookup, priority, icon
    fun create(text: String): LookupElement =
        LookupElementBuilder.create(text)

    fun create(text: String, lookup: String): LookupElement =
        GdLookupElementBuilder
            .create(text, lookup)
            .withInsertHandler(GdLookupInsertHandler.INSTANCE);

    fun create(text: String, priority: Double): LookupElement =
        PrioritizedLookupElement.withPriority(
            this.create(text),
            priority
        );

    fun create(text: String, icon: Icon): LookupElement =
        GdLookupElementBuilder
            .create(text)
            .withIcon(icon);

    // text, lookup + priority, icon
    fun create(text: String, lookup: String, icon: Icon): LookupElement =
        GdLookupElementBuilder
            .create(text, lookup)
            .withIcon(icon)
            .withInsertHandler(GdLookupInsertHandler.INSTANCE);

    fun create(text: String, lookup: String, priority: Double): LookupElement =
        PrioritizedLookupElement.withPriority(
            this.create(text, lookup),
            priority
        );

    // text, priority, icon
    fun create(text: String, priority: Double, icon: Icon): LookupElement =
        PrioritizedLookupElement.withPriority(
            this.create(text, icon),
            priority
        );

    // text, lookup, priority, icon
    fun create(text: String, lookup: String, priority: Double, icon: Icon): LookupElement =
        PrioritizedLookupElement.withPriority(
            this.create(text, lookup, icon),
            priority
        );

}
