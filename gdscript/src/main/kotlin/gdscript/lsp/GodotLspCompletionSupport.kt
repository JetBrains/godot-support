package gdscript.lsp

import GdScriptPluginIcons
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.platform.lsp.api.customization.LspCompletionSupport
import com.intellij.platform.lsp.util.getOffsetInDocument
import gdscript.competion.utils.GdMethodParenthesesInsertHandler
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import javax.swing.Icon


/**
 * Customizes how Godot's LSP completion items are turned into IDE lookup elements.
 *
 * Behaviour we add on top of the default [LspCompletionSupport]:
 *  - GDScript-specific prefix handling for `$NodePaths` (see RIDER-119006).
 *  - For function/method items: strip the trailing `()` or lone `(` Godot sends in
 *    `insertText`/`textEdit.newText` so the IDE inserts parens itself via
 *    [GdMethodParenthesesInsertHandler] (smart caret + auto parameter info; see RIDER-134419).
 *  - For string-literal items (e.g. `"ui_accept"` in `is_action_pressed("ui_a<caret>")`):
 *      * pre-insertion: [preserveLeadingQuoteInRange] shifts a `textEdit.range` that would otherwise
 *        swallow the user's opening quote;
 *      * post-insertion: [stripDuplicateQuotesAdjacentToInserted] removes a duplicate quote that the
 *        platform replacement leaves adjacent to the inserted text.
 *
 * Open for testing (see `LspCompletionEndToEndTest`).
 */
open class GodotLspCompletionSupport : LspCompletionSupport() {
    override fun getCompletionPrefix(parameters: CompletionParameters, defaultPrefix: String): String =
        // RIDER-119006 LSP Completion for GDScript doesn't work after "$"
        if (defaultPrefix.startsWith("$")) defaultPrefix.substringAfter("$")
        else defaultPrefix

    override fun createLookupElement(parameters: CompletionParameters, item: CompletionItem): LookupElement? {
        val isFunction = item.isFunctionLike()
        if (isFunction) item.stripTrailingEmptyParens()
        item.preserveLeadingQuoteInRange(parameters)
        val base = super.createLookupElement(parameters, item) ?: return null
        val hasParams = isFunction && item.hasParameters()
        return LookupElementDecorator.withDelegateInsertHandler(base) { ctx, lk ->
            stripDuplicateQuotesAdjacentToInserted(ctx)
            if (isFunction) GdMethodParenthesesInsertHandler(hasParams).handleInsert(ctx, lk)
        }
    }

    // TODO: remove once IJPL-246856 CompletionItemKind.Event is missing an icon in autocompletion
    // is resolved, this is a temporary fix
    override fun getIcon(item: CompletionItem): Icon? {
        if (item.kind == CompletionItemKind.Event) {
            return GdScriptPluginIcons.GDScriptIcons.SIGNAL_MARKER
        }
        return super.getIcon(item)
    }
}

fun CompletionItem.isFunctionLike(): Boolean = when (kind) {
    CompletionItemKind.Function, CompletionItemKind.Method, CompletionItemKind.Constructor -> true
    else -> false
}

fun CompletionItem.hasParameters(): Boolean {
    val text = label ?: return true
    return !text.endsWith("()")
}

fun CompletionItem.stripTrailingEmptyParens() {
    fun strip(text: String): String = when {
        text.endsWith("()") -> text.substring(0, text.length - 2)
        text.endsWith("(")  -> text.substring(0, text.length - 1)
        else                -> text
    }
    insertText?.let { insertText = strip(it) }
    textEdit?.let { edit ->
        edit.map(
            { it.also { e -> e.newText = strip(e.newText) } },
            { it.also { e -> e.newText = strip(e.newText) } },
        )
    }
}

/**
 * Post-insertion dedup. After the platform replaces the prefix with the lookup string, the
 * inserted text lives at `[context.startOffset, context.tailOffset)`. If its first/last char is a
 * quote and the document already has the same quote on that side of the inserted range, delete
 * the duplicate.
 *
 *   "ui<caret>     + insertText `"ui_accept"`  ->  delete the leading "
 *   "ui_<caret>")  + insertText `ui_accept"`   ->  delete the trailing "
 */
fun stripDuplicateQuotesAdjacentToInserted(context: InsertionContext) {
    val doc = context.document
    val chars = doc.charsSequence
    val start = context.startOffset
    var tail = context.tailOffset
    if (tail <= start) return

    if (tail < chars.length) {
        val last = chars[tail - 1]
        if ((last == '"' || last == '\'') && chars[tail] == last) {
            doc.deleteString(tail - 1, tail)
            tail -= 1
            context.tailOffset = tail
        }
    }
    if (start in 1..<tail) {
        val first = chars[start]
        if ((first == '"' || first == '\'') && chars[start - 1] == first) {
            doc.deleteString(start, start + 1)
            context.tailOffset = tail - 1
        }
    }
}

/**
 * Godot's `completionItem/resolve` for string literals sometimes returns a `textEdit` whose range
 * covers the user's surrounding quotes but a `newText` without them. The platform replaces
 * `[range.start, caret)`, which would swallow the opening quote. Shift `range.start` past the
 * quote so the user's quote survives the replacement.
 */
fun CompletionItem.preserveLeadingQuoteInRange(parameters: CompletionParameters) {
    val edit = textEdit ?: return
    val newText = edit.map({ it.newText }, { it.newText })
    if (newText.isEmpty()) return
    val first = newText[0]
    if (first == '"' || first == '\'') return  // newText already has a leading quote; dedup handles it.

    val doc = parameters.editor.document
    val range = edit.map({ it.range }, { it.insert })
    val startOffset = getOffsetInDocument(doc, range.start) ?: return
    if (startOffset >= doc.textLength) return
    val q = doc.charsSequence[startOffset]
    if (q != '"' && q != '\'') return

    range.start.character += 1
}