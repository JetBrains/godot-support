package com.jetbrains.godot.gdscript.lsp

import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProcess
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.OffsetMap
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.application.EDT
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.lsp.GodotLspCompletionSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import org.eclipse.lsp4j.Position
import org.eclipse.lsp4j.Range
import org.eclipse.lsp4j.TextEdit
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 * Tests the public completion behavior of [GodotLspCompletionSupport].
 *
 * Insertion tests create a lookup element, simulate the platform's prefix replacement,
 * and call the insert handler. Other tests check item filtering, payload preservation,
 * and completion prefixes directly.
 *
 * Cases:
 *  1. A parameterless function inserts `test()`.
 *  2. A function with parameters inserts parentheses and places the caret inside them.
 *  3. A constructor with parameters removes the server's trailing `(` before inserting parentheses.
 *  4. A quoted item without `textEdit` inserts only its content, without quotes or a `&` or `^` prefix.
 *  5. An unquoted item inside a string is hidden.
 *  6. For an item with `textEdit`, the string-content rule does not change the replacement text or range.
 *  7. A completion prefix keeps `$` inside a string but removes it from node path syntax.
 *
 * Cases 1 to 3 come from [RIDER-134419](https://youtrack.jetbrains.com/issue/RIDER-134419).
 * These tests check observable behavior without depending on the transformation helpers.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["LSP functionality/Completion"])
@TestApplication
class LspCompletionEndToEndTest {

    private val gd: GdFixture by gdLightFixture("gdscript/completion")

    private val customizer = GodotLspCompletionSupport()

    /**
     * Configures the fixture from a source containing a `<caret>` marker and returns the offset
     * of that marker.
     */
    private fun setupDocument(sourceWithCaret: String): Int {
        gd.fixture.configureByText("Test.gd", sourceWithCaret)
        return gd.fixture.caretOffset
    }

    /**
     * Build `CompletionParameters` for the current caret position, equivalent to what the IDE
     * builds when running the BASIC completion contributor at that offset.
     */
    private fun makeParams(caretOffset: Int): CompletionParameters {
        val position = gd.fixture.file.findElementAt(caretOffset.coerceAtMost(gd.fixture.file.textLength - 1).coerceAtLeast(0))
            ?: gd.fixture.file
        return CompletionParameters(
            /* position           = */ position,
            /* originalFile       = */ gd.fixture.file,
            /* completionType     = */ CompletionType.BASIC,
            /* offset             = */ caretOffset,
            /* invocationCount    = */ 1,
            /* editor             = */ gd.fixture.editor,
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
        val editor = gd.fixture.editor
        val document = editor.document
        val caret = editor.caretModel.offset
        val lookupString = lookupElement.lookupString

        // 1. Platform-style prefix replacement: replace [prefixStart, caret) with lookupString.
        WriteCommandAction.runWriteCommandAction(gd.project) {
            document.replaceString(prefixStart, caret, lookupString)
            PsiDocumentManager.getInstance(gd.project).commitDocument(document)
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
            gd.fixture.file,
            editor,
            /* addCompletionChar = */ false,
        )

        // 3. Run the insert handler inside a write-command action (the helpers may modify the doc).
        WriteCommandAction.runWriteCommandAction(gd.project) {
            lookupElement.handleInsert(ctx)
            PsiDocumentManager.getInstance(gd.project).commitDocument(document)
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
    // Function and constructor completion
    // ----------------------------------------------------------------------

    @Test
    fun `Case 1 - parameterless function - inserts parentheses`() = timeoutRunBlocking {
        // `tes<caret>` + LSP sends `test()` (CompletionItem.label/insertText both "test()", kind=Function).
        val item = CompletionItem("test()").apply {
            kind = CompletionItemKind.Function
            insertText = "test()"
        }
        val (text, _) = withContext(Dispatchers.EDT) {
            runCompletion(
                sourceWithCaret = "tes<caret>",
                item = item,
                prefixStart = 0, // user-typed `tes` will be replaced by the lookupString `test`
            )
        }
        assertEquals("test()", text)
    }

    // TODO Cover completion before existing parentheses: `tes<caret>()` + `test()` → `test()`.
    // The harness does not yet cover this: the test stub for prefix replacement does not model IDENTIFIER_END_OFFSET.
    // ParenthesesInsertHandler uses this offset to decide whether to reuse an existing `(`.
    // The behavior works in production.
    // Add coverage when the harness exposes IDENTIFIER_END_OFFSET and SELECTION_END_OFFSET correctly.

    @Test
    fun `Case 2 - function with parameters - caret lands inside parentheses`() = timeoutRunBlocking {
        // `test_wit<caret>` + LSP sends `test_with_args` (no parens in label/insertText — that's how
        // Godot signals a non-empty parameter list). Result should be `test_with_args(<caret>)`.
        val item = CompletionItem("test_with_args").apply {
            kind = CompletionItemKind.Function
            insertText = "test_with_args"
        }
        val (text, caret) = withContext(Dispatchers.EDT) {
            runCompletion(
                sourceWithCaret = "test_wit<caret>",
                item = item,
                prefixStart = 0,
            )
        }
        assertEquals("test_with_args()", text)
        assertEquals("test_with_args(".length, caret, "caret should be inside parens")
    }

    @Test
    fun `Case 3 - constructor with parameters - trailing open parenthesis is stripped`() = timeoutRunBlocking {
        // RIDER-132535: Godot sends label = "new(…)", insertText = "new(" for constructor-with-args.
        // Without the lone-paren strip we'd insert `new(` and then ParenthesesInsertHandler would
        // append `()` on top, producing `new(()`. The caret should land inside the parentheses.
        val item = CompletionItem("new(…)").apply {
            kind = CompletionItemKind.Method
            insertText = "new("
        }
        val (text, caret) = withContext(Dispatchers.EDT) {
            runCompletion(
                sourceWithCaret = "ne<caret>",
                item = item,
                prefixStart = 0,
            )
        }
        assertEquals("new()", text)
        assertEquals("new(".length, caret, "caret should be inside parens")
    }

    // ----------------------------------------------------------------------
    // String completion
    // ----------------------------------------------------------------------

    @ParameterizedTest(name = "{0}{1}ju<caret>{1}, suggestion={2}")
    @MethodSource("quotedItems")
    fun `Case 4 - string item without textEdit - inserts content without quotes`(
        prefix: String,
        quote: String,
        suggestion: String,
    ): Unit = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val source = "var s = $prefix${quote}ju<caret>$quote"
            val item = CompletionItem("original label").apply {
                kind = CompletionItemKind.Function
                insertText = suggestion
            }
            val (text, _) = runCompletion(source, item, source.indexOf("ju"))
            assertEquals("var s = $prefix${quote}jump()$quote", text)
            assertEquals("original label", item.label)
        }
    }

    @ParameterizedTest(name = "{0}{1}<caret>{1}")
    @MethodSource("hostLiterals")
    fun `Case 5 - unquoted item inside string - hidden`(prefix: String, quote: String): Unit = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            // Godot can send an unquoted enum value, for example Node.PROCESS_MODE_INHERIT for node.set_process_mode("<caret>").
            // As string content, it would become the String "Node.PROCESS_MODE_INHERIT", not the enum value. So the plugin hides it.
            val caret = setupDocument("var s = $prefix${quote}<caret>$quote")
            val unquoted = CompletionItem("Node.PROCESS_MODE_INHERIT")
            assertNull(customizer.createLookupElement(makeParams(caret), unquoted))
        }
    }

    @Test
    fun `Case 6 - string item with textEdit - payload unchanged`(): Unit = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            // With textEdit, the server defines the replaced range and the new text. The string-content-only rule does not change them.
            val caret = setupDocument("var s = 'ju<caret>'")
            val edit = TextEdit(Range(Position(0, 8), Position(0, 12)), "'jump'")
            val item = CompletionItem("jump").apply {
                kind = CompletionItemKind.Text
                textEdit = Either.forLeft(edit)
            }
            assertNotNull(customizer.createLookupElement(makeParams(caret), item))
            assertEquals("'jump'", edit.newText)
            assertEquals(Range(Position(0, 8), Position(0, 12)), edit.range)
            assertNull(item.insertText)
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("dollarPrefixes")
    fun `Case 7 - dollar prefix - preserved inside strings only`(source: String, expected: String): Unit = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            // Inside a string, `$` is content. Only node path syntax outside a string removes it from the prefix (RIDER-119006).
            val caret = setupDocument(source)
            assertEquals(expected, customizer.getCompletionPrefix(makeParams(caret), "\$Ch"))
        }
    }

    companion object {
        private val hostQuoteForms = listOf("" to "\"", "&" to "\"", "r" to "\"", "" to "\"\"\"")

        @JvmStatic
        private fun quotedItems(): List<Arguments> = hostQuoteForms.flatMap { (prefix, quote) ->
            listOf("\"jump()\"", "'jump()'", "&\"jump()\"", "^'jump()'").map { suggestion ->
                Arguments.of(prefix, quote, suggestion)
            }
        }

        @JvmStatic
        private fun hostLiterals(): List<Arguments> = hostQuoteForms.map { (prefix, quote) -> Arguments.of(prefix, quote) }

        @JvmStatic
        private fun dollarPrefixes(): List<Arguments> = listOf(
            Arguments.of("var s = \"\$Ch<caret>\"", "\$Ch"),
            Arguments.of("var n = \$Ch<caret>", "Ch"),
        )
    }
}
