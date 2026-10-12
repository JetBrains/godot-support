package com.jetbrains.godot.gdscript.formatter

import com.intellij.application.options.CodeStyle
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.pom.PomManager
import com.intellij.pom.event.PomModelEvent
import com.intellij.pom.impl.PomTransactionBase
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdStmt
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdLambdaTailFormattingTest : BasePlatformTestCase() {
    /**
     * Describes the line after a lambda body that starts with a comma or a closing bracket.
     * [call] and [lambdaLine] match text within the call line and the lambda reference line.
     * [body] and [lastBodyLine] match the trimmed first and last body lines.
     * The [lambdaLine] marker can identify an earlier item line used for comma alignment.
     * [callLineOutsideExpression] selects the body column for a closing bracket.
     * [commaOneIndentRight] places a comma four columns to the right of the lambda reference line.
     */
    private data class Tail(
        val call: String,
        val body: String = "print(12)",
        val lastBodyLine: String = body,
        val commaOneIndentRight: Boolean = false,
        val callLineOutsideExpression: Boolean = false,
        val lambdaLine: String = call,
    )

    private data class Settings(
        val tabs: Boolean = false,
        val continuationSize: Int = 8,
        val continuationIndent: Boolean = true,
        val hangClosingBrackets: Boolean = true,
        val bracketOnNextLine: Boolean = true,
    )

    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript/formatter").pathString

    @Test
    fun testFirstArgument() = checkMatrix(
        """
        func hi():
            f(func():
                print(12)
            , 2)
        """, Tail("f(func():")
    )

    @Test
    fun testMiddleArgumentAtBodyColumn() = checkMatrix(
        """
        func hi():
            f(1, func():
                print(12)
                , 3, 4)
        """, Tail("f(1, func():", commaOneIndentRight = true)
    )

    @Test
    fun testCommaBelowCallLine() = checkMatrix(
        """
        func hi():
            f(func():
                print(12)
          , 2)
        """, Tail("f(func():")
    )

    @Test
    fun testLastArgument() = checkMatrix(
        """
        func hi():
            f(1, func():
                print(12)
                )
        """, Tail("f(1, func():")
    )

    @Test
    fun testVarInitializer() = checkMatrix(
        """
        func hi():
            var value = f(func():
                print(12)
                )
        """, Tail("var value = f(")
    )

    @Test
    fun testClassLevelVarInitializer() = checkMatrix(
        """
        var value = f(func():
            print(12)
        )
        """, Tail("var value = f(")
    )

    @Test
    fun testTwoLambdas() = checkMatrix(
        """
        func hi():
            f(func():
                print(12)
                , func():
                    print(13)
            )
        """, Tail("f(func():", commaOneIndentRight = true), Tail("f(func():", "print(13)")
    )

    @Test
    fun testOwnLineLambdaCommaAtLambdaLine() = checkMatrix(
        """
        func hi():
            f(
                    func():
                        print(12)
                    , 0)
        """, Tail("f(", lambdaLine = "func():")
    )

    @Test
    fun testOwnLineLambdaCommaOneIndentRight() = checkMatrix(
        """
        func hi():
            f(
                    func():
                        print(12)
                        , 0)
        """, Tail("f(", commaOneIndentRight = true, lambdaLine = "func():")
    )

    @Test
    fun testCommaAfterSecondLambda() = checkMatrix(
        """
        func hi():
            f(
                    func(first):
                        print(12)
                        , func(second):
                            print(13)
                          , 0)
        """, Tail("f(", commaOneIndentRight = true, lambdaLine = "func(first):"),
        Tail("f(", "print(13)", commaOneIndentRight = true)
    )

    @Test
    fun testCommaAtCallLineAfterSecondLambda() = checkMatrix(
        """
        func hi():
            f(func(first):
                print(12)
                , func(second):
                    print(13)
            , 0)
        """, Tail("f(", commaOneIndentRight = true), Tail("f(", "print(13)")
    )

    @Test
    fun testItemLineBeforeLambdaLine() = checkMatrix(
        """
        func hi():
            f(
                    a +
                            func():
                                print(12)
                    , 0)
        """, Tail("f(", lambdaLine = "a +")
    )

    @Test
    fun testItemLineBeforeLambdaLineCommaOneIndentRight() = checkMatrix(
        """
        func hi():
            f(
                    a +
                            func():
                                print(12)
                        , 0)
        """, Tail("f(", commaOneIndentRight = true, lambdaLine = "a +")
    )

    @Test
    fun testCallLineItemBeforeLambdaLine() = checkMatrix(
        """
        func hi():
            f(a +
                    func():
                        print(12)
                  , 0)
        """, Tail("f(", commaOneIndentRight = true)
    )

    @Test
    fun testNestedCall() = checkMatrix(
        """
        func hi():
            outer(inner(1, func():
                print(12)
                , 2), 3)
        """, Tail("outer(inner(", commaOneIndentRight = true)
    )

    @Test
    fun testMethodChain() = checkMatrix(
        """
        func hi():
            button.pressed.connect(func():
                print(12)
            )
        """, Tail("button.pressed.connect(")
    )

    @Test
    fun testMultilineMethodChain() = checkMatrix(
        """
        func hi():
            var result = (button
                .pressed.connect(func():
                    print(12)
                )
                .next())
        """, Tail(".pressed.connect(")
    )

    @Test
    fun testArrayComma() = checkMatrix(
        """
        func hi():
            var a = [1, func():
                print(12)
                , 2]
        """, Tail("var a = [", commaOneIndentRight = true)
    )

    @Test
    fun testArrayClose() = checkMatrix(
        """
        func hi():
            var a = [func():
                print(12)
                ]
        """, Tail("var a = [")
    )

    @Test
    fun testDictionaryComma() = checkMatrix(
        """
        func hi():
            var d = {"a": func():
                print(12)
            , "b": 2}
        """, Tail("var d = {")
    )

    @Test
    fun testDictionaryClose() = checkMatrix(
        """
        func hi():
            var d = {callback = func():
                print(12)
                }
        """, Tail("var d = {")
    )

    @Test
    fun testFileLevelArray() = checkMatrix(
        """
        var a = [func():
            print(12)
        ]
        """, Tail("var a = [")
    )

    @Test
    fun testFileLevelDictionary() = checkMatrix(
        """
        var d = {"a": func():
            print(12)
        }
        """, Tail("var d = {")
    )

    @Test
    fun testBlankAndCommentBeforeComma() = checkMatrix(
        """
        func hi():
            f(func():
                print(12)

                # Keep the callback.
                , 2)
        """, Tail("f(func():", commaOneIndentRight = true)
    )

    @Test
    fun testBlankAndCommentBeforeClose() = checkMatrix(
        """
        func hi():
            f(func():
                print(12)

                # Keep the callback.
            )
        """, Tail("f(func():")
    )

    @Test
    fun testCallLineDeeperThanBody() = checkMatrix(
        """
        func hi():
            var value = [
                    1] + f(func():
                print(12)
            )
        """, Tail("f(func():", callLineOutsideExpression = true)
    )

    @Test
    fun testMultilineStringPrefixOutput() {
        for (tabs in listOf(false, true)) {
            val indent = if (tabs) "\t" else "    "
            val source = "var value = \"\"\"first\nlast\"\"\" + f(func():\n${indent}pass\n)\n"
            val expected = "var value = \"\"\"first\nlast\"\"\" + f(func():\n${indent}pass\n${indent})\n"
            withSettings(Settings(tabs = tabs)) {
                assertEquals(expected, checkTwoPasses(source, "multiline string prefix tabs=$tabs"))
            }
        }
    }

    @Test
    fun testMultilineStringBeforeCallClose() = checkMatrix(
        "var value = \"\"\"first\nlast\"\"\" + f(func():\n    print(12)\n)\n",
        Tail("last\"\"\" + f(", callLineOutsideExpression = true)
    )

    @Test
    fun testMultilineStringBeforeCallComma() = checkMatrix(
        "var value = \"\"\"first\nlast\"\"\" + f(func():\n    print(12)\n  , 2)\n",
        Tail("last\"\"\" + f(", commaOneIndentRight = true, callLineOutsideExpression = true)
    )

    @Test
    fun testMultilineStringAndCommentInBody() = checkMatrix(
        "func hi():\n    f(func():\n        var value = \"\"\"first\nlast\"\"\"\n\n        # Keep the callback.\n    )\n",
        Tail("f(", body = "var value = \"\"\"first", lastBodyLine = "last\"\"\"")
    )

    @Test
    fun testNestedSuiteInLambda() = checkMatrix(
        """
        func hi():
            f(func():
                if true:
                    print(12)
            )
        """, Tail("f(func():", "if true:", "print(12)")
    )

    @Test
    fun testReformatInsidePsiTransaction() {
        val source = "func hi():\n    pass\n"
        val replacementSource = """
            func hi():
                f(func():
                    print(12)
                  , func():
                        print(13)
                    )
        """.trimIndent() + "\n"
        val expected = """
            func hi():
                f(func():
                    print(12)
                    , func():
                            print(13)
                )
        """.trimIndent() + "\n"
        withSettings(Settings(hangClosingBrackets = false)) {
            myFixture.configureByText("lambda_tail.gd", source)
            val file = myFixture.file
            val document = myFixture.editor.document
            val replacementFile = PsiFileFactory.getInstance(project)
                .createFileFromText("replacement.gd", GdLanguage, replacementSource)
            val statement = PsiTreeUtil.findChildrenOfType(file, GdStmt::class.java).single { it.text.trim() == "pass" }
            val replacement = PsiTreeUtil.findChildrenOfType(replacementFile, GdStmt::class.java)
                .single { it.text.startsWith("f(") }
            WriteCommandAction.runWriteCommandAction(project) {
                val model = PomManager.getModel(project)
                model.runTransaction(object : PomTransactionBase(file) {
                    override fun runInner(): PomModelEvent {
                        val manager = CodeStyleManager.getInstance(project)
                        manager.performActionWithFormatterDisabled { statement.replace(replacement) }
                        assertEquals("The document stays unchanged inside the transaction", source, document.text)
                        assertTrue("The PSI offsets exceed the document length", file.textLength > document.textLength)
                        assertNoParseErrors("transaction input")
                        manager.reformat(file)
                        assertNoParseErrors("transaction output")
                        assertEquals(expected, file.text)
                        return PomModelEvent(model, null)
                    }
                })
                PsiDocumentManager.getInstance(project).commitAllDocuments()
            }
            assertEquals(expected, document.text)
            reformat()
            assertNoParseErrors("transaction second pass")
            assertEquals(expected, file.text)
        }
    }

    @Test fun testRider140440Fixture() = checkFixture("lambdaTailRider140440")

    private fun checkFixture(name: String) {
        withSettings(Settings()) {
            myFixture.configureByFile("$name.gd")
            checkTwoPasses(myFixture.file.text, name)
            myFixture.checkResultByFile("${name}_after.gd")
        }
    }

    /**
     * Checks all 32 settings combinations for each [Tail].
     * The matrix combines tabs or spaces, continuation sizes 4 or 8, and three switches.
     * The switches control continuation indentation, hanging brackets, and bracket line breaks.
     * Each run checks the input and both formatted passes for parse errors.
     * It also checks that the second pass is stable and calls [checkTailColumn] for each tail.
     */
    private fun checkMatrix(source: String, vararg tails: Tail) {
        val switches = listOf(false, true)
        val settingsMatrix = switches.flatMap { tabs ->
            listOf(4, 8).flatMap { continuationSize ->
                switches.flatMap { continuationIndent ->
                    switches.flatMap { hangClosingBrackets ->
                        switches.map { bracketOnNextLine ->
                            Settings(tabs, continuationSize, continuationIndent, hangClosingBrackets, bracketOnNextLine)
                        }
                    }
                }
            }
        }
        for (settings in settingsMatrix) {
            val label = "${getTestName(false)} $settings"
            val input = source.trimIndent().lines().joinToString("\n", postfix = "\n") { line ->
                // Round partial indents up to whole tabs to keep each input free of mixed indentation.
                if (settings.tabs) "\t".repeat((line.takeWhile { it == ' ' }.length + 3) / 4) + line.trimStart() else line
            }
            withSettings(settings) {
                val output = checkTwoPasses(input, label)
                for (tail in tails) checkTailColumn(output, tail, label)
            }
        }
    }

    /**
     * Checks the first code line after [Tail.lastBodyLine]. It must start with `,`, `)`, `]`, or `}`.
     * A comma uses the lambda reference line column, plus four columns when [Tail.commaOneIndentRight] is set.
     * A closing bracket uses the body column when [Tail.callLineOutsideExpression] is set.
     * Otherwise, a closing bracket uses the smaller of the call column and the body column.
     * The tail column must not exceed the body column. Tabs use stops of four columns.
     */
    private fun checkTailColumn(output: String, tail: Tail, label: String) {
        val lines = output.lines()
        val callLine = lines.single { it.contains(tail.call) }
        val bodyLine = lines.single { it.trim() == tail.body }
        val lambdaLine = lines.single { it.contains(tail.lambdaLine) }
        val lastBodyIndex = lines.indexOfFirst { it.trim() == tail.lastBodyLine }
        val tailLine = lines.drop(lastBodyIndex + 1).first { it.isNotBlank() && !it.trimStart().startsWith('#') }
        val token = tailLine.trimStart().first()
        assertTrue("$label: missing tail line\n$output", token in ",)]}")
        val expected = when {
            token == ',' -> column(lambdaLine) + if (tail.commaOneIndentRight) 4 else 0
            tail.callLineOutsideExpression -> column(bodyLine)
            else -> minOf(column(callLine), column(bodyLine))
        }
        assertEquals("$label: tail column\n$output", expected, column(tailLine))
        assertTrue("$label: tail deeper than body\n$output", column(tailLine) <= column(bodyLine))
    }

    private fun column(line: String): Int = line.takeWhile { it == ' ' || it == '\t' }.fold(0) { column, char ->
        if (char == '\t') (column / 4 + 1) * 4 else column + 1
    }

    private fun checkTwoPasses(source: String, label: String): String {
        myFixture.configureByText("lambda_tail.gd", source)
        assertNoParseErrors("$label input")
        reformat()
        assertNoParseErrors("$label pass 1")
        val first = myFixture.file.text
        reformat()
        assertNoParseErrors("$label pass 2")
        assertEquals("$label second pass", first, myFixture.file.text)
        return first
    }

    private fun assertNoParseErrors(label: String) {
        val errors = PsiTreeUtil.findChildrenOfType(myFixture.file, PsiErrorElement::class.java)
        if (errors.isNotEmpty()) {
            val lexer = LanguageParserDefinitions.INSTANCE.forLanguage(GdLanguage).createLexer(myFixture.project)
            lexer.start(myFixture.file.text)
            val tokens = buildString {
                while (lexer.tokenType != null) {
                    appendLine("${lexer.tokenType} (${lexer.tokenStart},${lexer.tokenEnd})")
                    lexer.advance()
                }
            }
            fail("$label: ${errors.map { "${it.textRange}: ${it.errorDescription}" }}\n${myFixture.file.text}\n" +
                 tokens + DebugUtil.psiToString(myFixture.file, true))
        }
    }

    private fun reformat() {
        WriteCommandAction.runWriteCommandAction(myFixture.project) {
            CodeStyleManager.getInstance(myFixture.project).reformatText(myFixture.file, 0, myFixture.editor.document.textLength)
            PsiDocumentManager.getInstance(myFixture.project).commitAllDocuments()
        }
    }

    private fun withSettings(options: Settings, action: () -> Unit) {
        val settings = CodeStyle.createTestSettings(CodeStyle.getSettings(myFixture.project))
        settings.getCommonSettings(GdLanguage).apply {
            CALL_PARAMETERS_RPAREN_ON_NEXT_LINE = options.bracketOnNextLine
            indentOptions!!.apply {
                USE_TAB_CHARACTER = options.tabs
                TAB_SIZE = 4
                INDENT_SIZE = 4
                CONTINUATION_INDENT_SIZE = options.continuationSize
            }
        }
        settings.getCustomSettings(GdCodeStyleSettings::class.java).apply {
            USE_CONTINUATION_INDENT_FOR_ARGUMENTS = options.continuationIndent
            USE_CONTINUATION_INDENT_FOR_COLLECTIONS = options.continuationIndent
            HANG_CLOSING_BRACKETS = options.hangClosingBrackets
            ARRAY_NEW_LINE_BEFORE_RIGHT_BRACKET = options.bracketOnNextLine
            DICT_NEW_LINE_BEFORE_RIGHT_BRACE = options.bracketOnNextLine
        }
        CodeStyle.doWithTemporarySettings(myFixture.project, settings, Runnable { action() })
    }
}
