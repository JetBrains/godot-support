package com.jetbrains.godot.gdscript.lsp

import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.OffsetMap
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.codeInsight.completion.CompletionProcess
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.lsp.GodotLspCompletionSupport
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * End-to-end tests for `GodotLspCompletionSupport`, the LSP completion customizer used by the Godot
 * LSP integration. These tests deliberately target the **public** customizer surface
 * (`createLookupElement` + the resulting `LookupElement.handleInsert`), not the private
 * transformation helpers, so the helpers can be refactored / merged / deleted without breaking
 * the tests.
 *
 * Each test simulates what the IDE does when the user accepts a completion suggestion from the
 * Godot LSP server:
 *  1. The user's source is in [setupDocument] with `<caret>` marking the caret.
 *  2. We synthesize a [CompletionItem] mimicking what Godot's LSP would send.
 *  3. We call [GodotLspCompletionSupport.createLookupElement] to obtain a [com.intellij.codeInsight.lookup.LookupElement].
 *  4. We simulate the platform's prefix replacement (write the lookup's `lookupString` into the
 *     document over `[startOffset, caret)`) and then call `handleInsert(ctx)`.
 *  5. We assert the final document text.
 *
 * This way, future refactors that delete `stripDuplicateQuotesAdjacentToInserted` /
 * `preserveLeadingQuoteInRange` / `stripTrailingEmptyParens` and replace them with anything else
 * still must produce the same observable document state for the user-facing scenarios listed
 * below (see [RIDER-134419](https://youtrack.jetbrains.com/issue/RIDER-134419)):
 *
 *  1. `tes<caret>`            + LSP `test()`         → `test()<caret>`            (parameterless fn)
 *  2. `tes<caret>()`          + LSP `test()`         → `test()<caret>`            (no duplicate parens)
 *  3. `test_wit<caret>`       + LSP `test_with_args` → `test_with_args(<caret>)`  (caret inside parens)
 *  4. `is_action_pressed("ui_a<caret>` + LSP `"ui_accept"` → `is_action_pressed("ui_accept"<caret>`
 *
 * Cases 1–3 cover the parens path (which is fine today); case 4 covers the quote handling that
 * the user wants to simplify/replace.
 */
@RunWith(JUnit4::class)
class LspCompletionEndToEndTest : BasePlatformTestCase() {

    private val customizer = GodotLspCompletionSupport()

    /**
     * Configures the fixture from a source containing a `<caret>` marker and returns the offset
     * of that marker (so we don't need [BasePlatformTestCase.myFixture] elsewhere).
     */
    private fun setupDocument(sourceWithCaret: String): Int {
        myFixture.configureByText("Test.gd", sourceWithCaret)
        return myFixture.caretOffset
    }

    /**
     * Build `CompletionParameters` for the current caret position, equivalent to what the IDE
     * builds when running the BASIC completion contributor at that offset.
     */
    private fun makeParams(caretOffset: Int): CompletionParameters {
        val position = myFixture.file.findElementAt(caretOffset.coerceAtMost(myFixture.file.textLength - 1).coerceAtLeast(0))
            ?: myFixture.file
        return CompletionParameters(
            /* position           = */ position,
            /* originalFile       = */ myFixture.file,
            /* completionType     = */ CompletionType.BASIC,
            /* offset             = */ caretOffset,
            /* invocationCount    = */ 1,
            /* editor             = */ myFixture.editor,
            /* process            = */ object : CompletionProcess { override fun isAutopopupCompletion(): Boolean = false },
        )
    }

    /**
     * Mimic what the platform does after the user selects a [lookupElement] from the popup:
     *  - replace `[prefixStart, caret)` in the document with `lookupString`;
     *  - build an [InsertionContext] whose START_OFFSET/TAIL_OFFSET bracket the inserted text;
     *  - run `lookupElement.handleInsert(ctx)`;
     *  - commit and return the resulting document text together with the caret offset.
     */
    private fun simulateInsert(
        prefixStart: Int,
        lookupElement: com.intellij.codeInsight.lookup.LookupElement,
    ): Pair<String, Int> {
        val editor = myFixture.editor
        val document = editor.document
        val caret = editor.caretModel.offset
        val lookupString = lookupElement.lookupString

        // 1. Platform-style prefix replacement: replace [prefixStart, caret) with lookupString.
        WriteCommandAction.runWriteCommandAction(myFixture.project) {
            document.replaceString(prefixStart, caret, lookupString)
            PsiDocumentManager.getInstance(myFixture.project).commitDocument(document)
        }
        val newTail = prefixStart + lookupString.length
        editor.caretModel.moveToOffset(newTail)

        // 2. Build an InsertionContext mirroring the post-replacement state.
        val offsetMap = OffsetMap(document)
        offsetMap.addOffset(CompletionInitializationContext.START_OFFSET, prefixStart)
        val ctx = InsertionContext(
            offsetMap,
            Lookup.NORMAL_SELECT_CHAR,
            arrayOf(lookupElement),
            myFixture.file,
            editor,
            /* addCompletionChar = */ false,
        )

        // 3. Run the insert handler inside a write-command action (the helpers may modify the doc).
        WriteCommandAction.runWriteCommandAction(myFixture.project) {
            lookupElement.handleInsert(ctx)
            PsiDocumentManager.getInstance(myFixture.project).commitDocument(document)
        }

        return document.text to editor.caretModel.offset
    }

    /** Convenience: configure document, build params + lookup, insert, return (text, caret). */
    private fun runCompletion(
        sourceWithCaret: String,
        item: CompletionItem,
        prefixStart: Int = -1,
    ): Pair<String, Int> {
        val caret = setupDocument(sourceWithCaret)
        val params = makeParams(caret)
        val lookup = customizer.createLookupElement(params, item)
            ?: error("createLookupElement returned null for item=$item")
        // Default: prefix starts where the user-visible prefix would naturally start
        // (here = caret, i.e. no prefix replacement). Tests that need to model "user typed
        // some prefix" should pass an explicit [prefixStart].
        val effectivePrefixStart = if (prefixStart >= 0) prefixStart else caret
        return simulateInsert(effectivePrefixStart, lookup)
    }

    // ----------------------------------------------------------------------
    // Parens path — currently working, regression-locked here.
    // ----------------------------------------------------------------------

    @Test
    fun `Case 1 - parameterless fn - LSP test_paren_inserts_parens`() {
        // `tes<caret>` + LSP sends `test()` (CompletionItem.label/insertText both "test()", kind=Function).
        val item = CompletionItem("test()").apply {
            kind = CompletionItemKind.Function
            insertText = "test()"
        }
        val (text, _) = runCompletion(
            sourceWithCaret = "tes<caret>",
            item = item,
            prefixStart = 0, // user-typed `tes` will be replaced by the lookupString `test`
        )
        assertEquals("test()", text)
    }

    // TODO Case 2 (`tes<caret>()` + LSP `test()` → `test()` — no duplicate parens) is not yet
    //  covered by this harness: the test stub for "prefix replacement" doesn't model the platform's
    //  IDENTIFIER_END_OFFSET, which is what `ParenthesesInsertHandler` actually consults to decide
    //  whether to reuse an existing `(`. The behaviour works in production. Add coverage once the
    //  harness exposes IDENTIFIER_END_OFFSET / SELECTION_END_OFFSET correctly.

    @Test
    fun `Case 3 - fn with parameters - caret lands inside parens`() {
        // `test_wit<caret>` + LSP sends `test_with_args` (no parens in label/insertText — that's how
        // Godot signals a non-empty parameter list). Result should be `test_with_args(<caret>)`.
        val item = CompletionItem("test_with_args").apply {
            kind = CompletionItemKind.Function
            insertText = "test_with_args"
        }
        val (text, caret) = runCompletion(
            sourceWithCaret = "test_wit<caret>",
            item = item,
            prefixStart = 0,
        )
        assertEquals("test_with_args()", text)
        assertEquals("caret should be inside parens", "test_with_args(".length, caret)
    }

    // ----------------------------------------------------------------------
    // Quote path — the part the user wants to simplify. These tests pin the
    // user-visible behaviour so refactors are safe.
    // ----------------------------------------------------------------------

    @Test
    fun `Case 5 - constructor with params - lone trailing open paren is stripped`() {
        // RIDER-132535: Godot sends label = "new(…)", insertText = "new(" for constructor-with-args.
        // Without the lone-paren strip we'd insert `new(` and then ParenthesesInsertHandler would
        // append `()` on top, producing `new(()`. Expected end state mirrors Case 3.
        val item = CompletionItem("new(…)").apply {
            kind = CompletionItemKind.Method
            insertText = "new("
        }
        val (text, caret) = runCompletion(
            sourceWithCaret = "ne<caret>",
            item = item,
            prefixStart = 0,
        )
        assertEquals("new()", text)
        assertEquals("caret should be inside parens", "new(".length, caret)
    }

    @Test
    fun `Case 4 - string literal completion - no duplicate opening quote`() {
        // `is_action_pressed("ui_a<caret>` + LSP sends a CompletionItem whose insertText is the
        // **quoted** action name `"ui_accept"`. The user already typed an opening `"`; after
        // accepting, the document must contain exactly one opening quote and one closing quote.
        //
        // Platform model: prefix = `"ui_a` (5 chars before caret), gets replaced by lookupString
        // (`"ui_accept"`). Without dedup we'd end up with `""ui_accept"`; with dedup it should be
        // `"ui_accept"`.
        val src = """is_action_pressed("ui_a<caret>)"""
        val caretIdx = src.indexOf("<caret>")
        val openingQuoteOffset = caretIdx - "ui_a".length - 1

        val item = CompletionItem("ui_accept").apply {
            kind = CompletionItemKind.Text
            insertText = "\"ui_accept\""
            filterText = "ui_accept"
        }
        val (text, _) = runCompletion(
            sourceWithCaret = src,
            item = item,
            // The platform replaces from the opening quote (which the user typed) up to the caret.
            // This is the offset model that triggers the helper's leading-quote dedup.
            prefixStart = openingQuoteOffset,
        )
        assertEquals("is_action_pressed(\"ui_accept\")", text)
    }
}
