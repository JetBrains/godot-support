package com.jetbrains.godot.gdscript.formatter

import com.intellij.application.options.CodeStyle
import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.lang.LanguageFormatting
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_BACKSPACE
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ex.EditorSettingsExternalizable
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.formatter.GdCodeStyleSettings
import gdscript.formatter.GdFormattingModelBuilder
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

/** Infrastructure copied from PyEditingTest */
@RunWith(JUnit4::class)
class GdEditingTest : BasePlatformTestCase() {
    override fun setUp() {
        super.setUp()
        LanguageFormatting.INSTANCE.addExplicitExtension(
            GdLanguage, GdFormattingModelBuilder(), testRootDisposable
        )
    }

    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript").pathString

    fun getCodeStyleSettings(): CodeStyleSettings = CodeStyle.getSettings(myFixture.project)

    fun getCommonCodeStyleSettings(): CommonCodeStyleSettings = getCodeStyleSettings().getCommonSettings(GdLanguage)

    fun getGdCodeStyleSettings(): GdCodeStyleSettings = getCodeStyleSettings().getCustomSettings(GdCodeStyleSettings::class.java)

    private fun doEnterTest() {
        val testName = "editing/" + getTestName(true)
        myFixture.configureByFile("$testName.gd")
        myFixture.performEditorAction(IdeActions.ACTION_EDITOR_ENTER)
        myFixture.checkResultByFile("${testName}_after.gd")
    }

    private fun doBackspaceTest() {
        val testName = "editing/" + getTestName(true)
        myFixture.configureByFile("$testName.gd")
        myFixture.performEditorAction(ACTION_EDITOR_BACKSPACE)
        myFixture.checkResultByFile("${testName}_after.gd")
    }

    private fun doTypingTest(character: Char) {
        val testName = "editing/" + getTestName(true)
        myFixture.configureByFile("$testName.gd")
        myFixture.type(character)
        myFixture.checkResultByFile("${testName}_after.gd")
    }

    private fun doTypingTest(text: String) {
        val testName = "editing/" + getTestName(true)
        myFixture.configureByFile("$testName.gd")
        myFixture.type(text)
        myFixture.checkResultByFile("${testName}_after.gd")
    }

    // Kept for the few multi-assertion tests below that exercise several offset/char pairs in one method.
    private fun doTestTyping(text: String, offset: Int, character: Char): String {
        val file: PsiFile = myFixture.configureByText(GdFileType, text)
        myFixture.getEditor().getCaretModel().moveToOffset(offset)
        myFixture.type(character)
        return myFixture.getDocument(file).getText()
    }

    // Kept for testRawStringAutomaticClosingQuotesRemoval (multi-step backspace).
    private fun doTestBackspace(before: String, after: String) {
        myFixture.configureByText("test.gd", before)
        myFixture.performEditorAction(ACTION_EDITOR_BACKSPACE)
        myFixture.checkResult(after)
    }

    /**
     * Clicks certain button in document on caret position
     *
     * @param action what button to click (const from [IdeActions])
     */
    fun pressButton(action: String) {
        CommandProcessor.getInstance().executeCommand(myFixture.getProject(), Runnable { myFixture.performEditorAction(action) }, "", null)
    }

    private fun doTestBackspace(fileName: String?, pos: LogicalPosition) {
        myFixture.configureByFile("editing/$fileName.gd")
        myFixture.getEditor().getCaretModel().moveToLogicalPosition(pos)
        pressButton(ACTION_EDITOR_BACKSPACE)
        myFixture.checkResultByFile("editing/${fileName}_after.gd", true)
    }

    @Test
    fun testNoPairedParenthesesBeforeIdentifier() = doTypingTest('(')  // PY-290

    @Test
    fun testPairedParenthesesAtEOF() = doTypingTest('(')

    @Test
    fun testPairedQuotesInRawString() = doTypingTest('\'')  // PY-263

    @Test
    @Ignore("TODO: requires lexer error-recovery for unclosed strings")
    fun testQuotesInString() = doTypingTest('"')  // PY-5041

    @Test
    fun testNonClosingQuoteAtIdent() = doTypingTest('"')  // PY-380

    @Test
    fun testNonClosingQuoteAtNumber() = doTypingTest('"')  // PY-380

    @Test
    fun testNonQuotedNodePathNoPairAfter() = doTypingTest('"')

    @Test
    fun testAutoClosingQuoteAtRBracket() = doTypingTest('"')

    @Test
    fun testAutoClosingQuoteAtRParen() = doTypingTest('"')

    @Test
    fun testAutoClosingQuoteAtComma() = doTypingTest('"')

    @Test
    fun testAutoClosingQuoteAtSpace() = doTypingTest('"')

    // --- Triple-quoted strings ---

    @Test
    fun testAutoCloseTriple() = doTypingTest('"')  // PY-1779

    @Test
    @Ignore("[quotes]")
    fun testAutoRemoveTriple() {  // PY-1779
        doTestBackspace("closedTripleQuoteBackspace", LogicalPosition(0, 11))
    }

    @Test
    fun testNoAutoclosingAtTheEnd() = doTypingTest('"')  // PY-19084

    @Test
    fun testAutoCloseAfterIllegalPrefix() = doTypingTest('"')

    // --- F-strings ---

    @Test
    fun testClosingQuotesCompletionForTripleQuotedRawString() = doTypingTest('"')  // PY-32872

    @Test
    fun testRawStringManuallyInsertedClosingQuotes() {  // PY-33901
        assertEquals("var s = r\"foo\"", doTestTyping("var s = r\"foo", 13, '"'))
        assertEquals("var s = r\"\"\"foo\"\"\"", doTestTyping("var s = r\"\"\"foo\"\"", 17, '"'))
    }

    @Test
    fun testRawStringQuoteInTextPartNotDuplicated() {  // PY-35434
        assertEquals("var s = r'\"]'", doTestTyping("var s = r']'", 10, '"'))
        assertEquals("var s = r'\" '", doTestTyping("var s = r' '", 10, '"'))
        assertEquals("var s = r'''foo\" '''", doTestTyping("var s = r'''foo '''", 15, '"'))
        assertEquals("var s = r'''foo\"\n'''", doTestTyping("var s = r'''foo\n'''", 15, '"'))
    }

    @Test
    @Ignore("[quotes]")
    fun testRawStringAutomaticClosingQuotesRemoval() {  // PY-35461
        doTestBackspace("var s = r'<caret>'", "var s = r")
        doTestBackspace("var s = r'''<caret>'''", "var s = r")
    }

    @Ignore("[parsing] [triplequotes]")
    @Test
    fun testEnterInTripleQuotedRawStringRightBeforeClosingQuotes() = doEnterTest()  // PY-32873

    // --- Overtype / pairing oddities ---

    @Test
    fun testOvertypeFromInside() = doTypingTest('"')

    @Test
    fun testInsertingColonRightBeforeParametersClosingParenthesisAtMultipleCarets() = doTypingTest(":")  // PY-31343

    @Test
    fun testPairedParenthesesMultipleCalls() {  // PY-21289
        doTypingTest('(')
    }

    // --- Backspace ---

    @Test
    fun testBackspaceDeindent() = doBackspaceTest()

    @Test
    fun testGreedyBackspace() {  // PY-254
        val settings = EditorSettingsExternalizable.getInstance()
        val oldVSpaceValue = settings.isVirtualSpace
        try {
            settings.isVirtualSpace = true
            doTestBackspace("greedyBackspace", LogicalPosition(4, 8))
        } finally {
            settings.isVirtualSpace = oldVSpaceValue
        }
    }

    @Test
    @Ignore("[smart-backspace]")
    fun testUnindentBackspace() {  // PY-853
        doTestBackspace("unindentBackspace", LogicalPosition(1, 1))
    }

    @Test
    fun testUnindentTab() {  // PY-1270
        doTestBackspace("unindentTab", LogicalPosition(3, 4))
    }

    // --- Comments ---

    @Ignore("[comment-continuation]")
    @Test
    fun testEnterInLineComment() = doEnterTest()  // PY-1739

    @Test
    fun testEnterAtStartOfComment() = doEnterTest()  // PY-1958

    @Test
    fun testEnterAtEndOfComment() = doEnterTest()  // PY-1958

    @Test
    fun testEnterBetweenCommentAndStatement() = doEnterTest()  // PY-1958

    @Test
    fun testEnterBeforeComment() = doEnterTest()  // PY-2138

    // --- Smart Enter — strings (implicit concat) ---

    @Test
    fun testEnterInPrefixString() = doEnterTest()  // PY-5058

    @Test
    fun testEnterInStringFormatting() = doEnterTest()  // PY-7039

    @Test
    fun testEnterInString() = doEnterTest()  // PY-1738

    @Test
    fun testEnterAfterStringPrefix() = doEnterTest()

    // A non-prefix word char glued to a string (only reachable mid-edit) must not be
    // duplicated onto the reopened fragment — the reopened literal carries only the
    // literal's own intrinsic prefix.
    @Test
    fun testEnterInWordAdjacentString() = doEnterTest()

    @Test
    fun testEnterInStringInParenth() = doEnterTest()

    @Test
    fun testEnterEscapedQuote() = doEnterTest()

    @Ignore("[parsing] - the result is not parsed correctly")
    @Test
    fun testEnterEscapedBackslash() = doEnterTest()

    @Test
    fun testEnterAfterSlash() = doEnterTest()

    @Test
    fun testStringFormatting() = doEnterTest()

    @Test
    fun testEndOfStringInParenth() = doEnterTest()

    @Test
    fun testSlashAfterSlash() = doEnterTest()

    @Test
    fun testEmptyStringInParenthesis() = doEnterTest()

    @Test
    fun testEmptyStringInParenthesis2() = doEnterTest()

    @Test
    fun testBracesInString() = doEnterTest()

    @Test
    fun testEnterOnOneLineMatchBlock() = doEnterTest()

    @Test
    fun testEnterOnLastParameter() = doEnterTest()

    @Test
    fun testEnterInMatch() = doEnterTest()

    @Ignore("[backslash]")
    @Test
    fun testEnterBeforeString() = doEnterTest()  // PY-3673

    @Test
    fun testBackslashInParenthesis() = doEnterTest()  // PY-5106

    @Test
    fun testSimpleIndent() {
        doEnterTest()
    }

    @Test
    fun testIndentAfterColon() {
        doEnterTest()
    }

    @Test
    fun testIndentAfterIf() {
        doEnterTest()
    }

    @Test
    fun testIndentInBody() {
        doEnterTest()
    }

    // --- Smart Enter — statements / keywords / decorators ---

    @Test
    fun testDedentAfterLastStatement() = doEnterTest()

    @Ignore("[backslash]")
    @Test
    fun testEnterInStatement() = doEnterTest()

    @Test
    fun testEnterBeforeStatement() = doEnterTest()

    @Test
    fun testEnterInParameterList() = doEnterTest()

    @Test
    fun testEnterInCodeWithErrorElements() = doEnterTest()

    @Test
    fun testEnterAfterBackslash() = doEnterTest()  // PY-1960

    @Test
    fun testEnterBetweenAnnotationAndFunction() = doEnterTest()  // PY-1985

    @Test
    fun testEnterInSubscriptionExpression() = doEnterTest()  // PY-1992

    @Test
    fun testEnterInEmptyFile() = doEnterTest()  // PY-2194

    @Test
    fun testEnterInKeyword() = doEnterTest()

    @Test
    fun testEnterInIdentifier() = doEnterTest()

    @Ignore("[backslash]")
    @Test
    fun testParenthesizedInIf() = doEnterTest()

    @Ignore("[backslash]")
    @Test
    fun testEnterAfterFuncKeywordInFunction() = doEnterTest()

    @Ignore("[backslash]")
    @Test
    fun testEnterBeforeColonInFunction() = doEnterTest()

    @Ignore("[backslash]")
    @Test
    fun testEnterBeforeArrowInFunction() = doEnterTest()  // PY-15469

    @Ignore("[backslash]")
    @Test
    fun testEnterAfterArrowInFunction() = doEnterTest()  // PY-15469

    @Test
    fun testEnterDoesNotInsertSlashInsideArrow() = doEnterTest()  // PY-15469

    @Test
    fun testEnterLastStatement() = doEnterTest()

    // --- Docstring stubs ---

    @Test
    fun testEnterInDocstring() = doEnterTest()  // CR-PY-144

    // --- Continuation indent in empty `()` `[]` `{}` ---

    @Test
    fun testContinuationIndentForFunctionArguments() = doEnterTest()  // PY-21478

    @Test
    fun testContinuationIndentInEmptyListLiteral() = doEnterTest()  // PY-20909

    @Test
    fun testContinuationIndentInEmptyDictLiteral() = doEnterTest()  // PY-20909

    // --- Injected language fragments ---

    // --- First-param insertion ---

    // --- Function type comment ---

    // --- Enter in incomplete literals (PY-10972) ---

    @Ignore("Requires [parser] changes")
    @Test
    fun testEnterInIncompleteListLiteral() {  // PY-10972
        doTypingTest("\n\"baz\"")
    }

    @Test
    @Ignore("[space-only]")
    fun testEnterInIncompleteListLiteral_Aligned() {  // PY-10972
        doTypingTest("\n\"baz\"")
    }

    @Ignore("Requires [parser] changes")
    @Test
    fun testEnterInIncompleteDictLiteral() {  // PY-10972
        doTypingTest("\n\"baz\"")
    }

    @Test
    @Ignore("[space-only]")
    fun testEnterInIncompleteDictLiteral_Aligned() {  // PY-10972
        doTypingTest("\n\"baz\"")
    }

    @Ignore("Requires [parser] changes")
    @Test
    fun testEnterInIncompleteNestedListLiteral() {  // PY-10972
        doTypingTest("\n\"baz\"")
    }

    @Test
    @Ignore("[space-only]")
    fun testEnterInIncompleteNestedListLiteral_Aligned() {  // PY-10972
        doTypingTest("\n\"baz\"")
    }

    // --- Tab-out from string literal ---

    @Test
    fun testTabOutFromStringLiteral() {
        val savedValue = CodeInsightSettings.getInstance().TAB_EXITS_BRACKETS_AND_QUOTES
        CodeInsightSettings.getInstance().TAB_EXITS_BRACKETS_AND_QUOTES = true
        try {
            myFixture.configureByText(
                "tabOutFromStringLiteral.gd",
                "func some():\n\tprint<caret>"
            )
            myFixture.type("(\"")
            myFixture.performEditorAction(IdeActions.ACTION_BRACE_OR_QUOTE_OUT)
            myFixture.checkResult("func some():\n\tprint(\"\"<caret>)")
            myFixture.performEditorAction(IdeActions.ACTION_BRACE_OR_QUOTE_OUT)
            myFixture.checkResult("func some():\n\tprint(\"\")<caret>")
        } finally {
            CodeInsightSettings.getInstance().TAB_EXITS_BRACKETS_AND_QUOTES = savedValue
        }
    }

    // --- Overtype colons ---

    @Test
    @Ignore("[colon-overtype]")
    fun testOvertypeColon() = doTypingTest(":")

    @Test
    fun testOvertypeColonInStringLiteral() = doTypingTest(":")

    // --- Match / case Enter ---

    @Test
    fun testEnterAfterColonOfMatchStatementWithoutClauses() {  // PY-48009
        doTypingTest('\n')
    }

    @Test
    fun testEnterAfterColonOfMatchStatementWithClauses() {  // PY-48009
        doTypingTest('\n')
    }

    @Test
    fun testEnterAfterColonOfCaseClauseWithoutBody() {  // PY-48009
        doEnterTest()
    }

    @Test
    fun testEnterAfterColonOfCaseClauseWithBody() {  // PY-48009
        doTypingTest('\n')
    }

    @Ignore("[backslash]")
    @Test
    fun testNoBackslashOnEnterInGroupPattern() {  // PY-49080
        doTypingTest('\n')
    }

    @Test
    fun testNoBackslashOnEnterInArrayPattern() {  // PY-49080
        doTypingTest('\n')
    }

    @Test
    @Ignore("[space-only]")
    fun testNoBackslashOnEnterInArrayPattern_Aligned() {  // PY-49080
        doEnterTest()
    }

    @Ignore("Requires [parser] changes - no way to know we are in dict decl with current parser error handling")
    @Test
    fun testEnterInDictKey() {
        doEnterTest()
    }

    @Ignore("Requires [parser] changes - no way to know we are in dict decl with current parser error handling")
    @Test
    fun testEnterInIncompleteDictKey() {
        doEnterTest()
    }

    @Test
    fun testNoBackslashOnEnterInDictPattern() {  // PY-49080
        doTypingTest('\n')
    }

    @Test
    @Ignore("[space-only]")
    fun testNoBackslashOnEnterInDictPattern_Aligned() {  // PY-49080
        doTypingTest('\n')
    }

    @Test
    fun testNoBackslashOnEnterInStringLiteralInsideArrayPattern() {  // PY-49080
        doTypingTest('\n')
    }

    @Test
    @Ignore("[space-only]")
    fun testNoBackslashOnEnterInStringLiteralInsideArrayPattern_Aligned() {  // PY-49080
        doTypingTest('\n')
    }


    @Test
    fun testNoBackslashOnEnterInStringLiteralInsideDictPattern() {  // PY-49080
        doTypingTest('\n')
    }

    @Test
    @Ignore("[space-only]")
    fun testNoBackslashOnEnterInStringLiteralInsideDictPattern_Aligned() {  // PY-49080
        doTypingTest('\n')
    }

    @Test
    fun testBackslashOnEnterInTopLevelStringLiteralPattern() = doEnterTest()  // PY-49080

    @Test
    @Ignore("[space-only]")
    fun testBackslashOnEnterInTopLevelStringLiteralPattern_Aligned() = doEnterTest()  // PY-49080

    // --- Parenthesized `with` Enter (PY-42200) ---

    // --- Parenthesise on Enter (`PARENTHESISE_ON_ENTER`) ---

    @Ignore("[parenthesise]")
    @Test
    fun testParenthesiseBinaryExpression() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Test
    @Ignore("[space-only]")
    fun testParenthesiseBinaryExpression_Aligned() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Ignore("[parenthesise]")
    @Test
    fun testParenthesiseCallChain() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Test
    @Ignore("[space-only]")
    fun testParenthesiseCallChain_Aligned() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Ignore("[parenthesise]")
    @Test
    fun testParenthesiseConditionalExpression() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Test
    @Ignore("[space-only]")
    fun testParenthesiseConditionalExpression_Aligned() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Test
    fun testParenthesiseString() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Test
    fun testStringNotParenthesisedRepeatedly() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Ignore("[backslash]")
    @Test
    fun testParenthesiseArrayPattern() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Test
    @Ignore("[space-only]")
    fun testParenthesiseArrayPattern_Aligned() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    @Ignore("[parenthesise]")
    @Test
    fun testParenthesiseAttributeAccessInCallChain() {
        getGdCodeStyleSettings().PARENTHESISE_ON_ENTER = true
        doEnterTest()
    }

    // --- Type-parameter list Enter (PY-61854) + new Dict-type test ---

    @Test
    fun testEnterInsideDictionaryTypeArgument() = doEnterTest()

    // --- Section A: StringName quote literals (&"...") ---

    @Test
    fun testStringNamePairing() = doTypingTest("\"")

    @Test
    fun testStringNameOvertypeClosingQuote() = doTypingTest("\"")

    @Test
    fun testStringNameBackspaceAtEmpty() = doBackspaceTest()

    @Test
    fun testStringNameNoPairBeforeIdent() = doTypingTest("\"")

    // --- Section B: NodePath quote literals (^"...") ---

    @Test
    fun testNodePathPairing() = doTypingTest("\"")

    @Test
    fun testNodePathOvertypeClosingQuote() = doTypingTest("\"")

    @Test
    fun testNodePathBackspaceAtEmpty() = doBackspaceTest()

    @Test
    fun testNodePathNoPairBeforeIdent() = doTypingTest("\"")

    // --- Section C: Property set/get blocks ---

    @Test
    fun testEnterAfterSetColonIndentsBody() = doEnterTest()

    @Test
    fun testEnterAfterGetColonIndentsBody() = doEnterTest()

    @Test
    fun testEnterAfterSetWithValueParamIndentsBody() = doEnterTest()

    @Test
    fun testEnterInSetGetSeparatorPreservesIndent() = doEnterTest()

    @Test
    fun testEnterAfterPropertyColonIndentsForShorthandBody() = doEnterTest()

    @Test
    fun testEnterAfterSingleLineShorthandPropertyDoesNotIndent() = doEnterTest()

    @Test
    fun testEnterAfterSetWithValueColonIndentsBodyDespiteSiblingGet() = doEnterTest()

    // --- Section D: ## doc-comments ---

    @Ignore("[comment-continuation]")
    @Test
    fun testEnterContinuesDocCommentOnNonBlankLine() = doEnterTest()


    @Test
    fun testEnterDoesNotContinueDocCommentOnBareMarker() = doEnterTest()

    @Ignore("[comment-continuation]")
    @Test
    fun testEnterAfterDocCommentBlockBeforeFunc() = doEnterTest()

    // --- Section E: match/when guards ---

    @Test
    fun testEnterAfterColonOfWhenGuardIndentsBody() = doEnterTest()

    @Test
    fun testEnterInsideWhenGuardExpression() = doEnterTest()

    @Test
    fun testEnterAfterTypedPatternWithWhenGuardIndentsBody() = doEnterTest()

    // --- Section G: Tab-only indent / backspace ---

    @Test
    fun testTabAtStartOfEmptyLineInsertsTab() {
        myFixture.configureByText("a.gd", "func main():\n<caret>\n\tpass")
        pressButton(IdeActions.ACTION_EDITOR_TAB)
        myFixture.checkResult("func main():\n\t<caret>\n\tpass")
    }

    @Test
    fun testBackspaceRemovesOneTabUnit() = doBackspaceTest()

    @Test
    fun testEnterAfterFuncColonIndentsWithSingleTab() = doEnterTest()

    @Test
    fun testEnterAfterFuncWithOneLineBody() = doEnterTest()

    @Test
    fun testEnterPreservesTabIndentInsideFunc() = doEnterTest()

    // --- Section H: Type-hint generics Array[T] ---

    @Test
    fun testBracketAutoCloseInArrayTypeHint() = doTypingTest("[")

    @Test
    fun testBracketAutoCloseInDictionaryReturnType() = doTypingTest("[")

    // --- Section K: Lambda func(): ... Enter ---

    @Test
    fun testEnterAfterLambdaColonIndentsBody() = doEnterTest()

    @Test
    fun testEnterInsideLambdaBodyPreservesIndent() = doEnterTest()

    // --- Section L: NodePath shorthand quote literals $"..." / %"..." ---

    @Test
    fun testDollarNodePathPairing() = doTypingTest("\"")

    @Test
    fun testDollarNodePathOvertypeClosingQuote() = doTypingTest("\"")

    @Test
    fun testDollarNodePathBackspaceAtEmpty() = doBackspaceTest()

    @Test
    fun testDollarNodePathNoPairBeforeIdent() = doTypingTest("\"")

    @Test
    fun testPercentUniqueNamePairing() = doTypingTest("\"")

    // --- Section M: #region / #endregion Enter ---

    @Test
    fun testEnterAfterRegionWithDescriptionDoesNotContinueComment() = doEnterTest()

    @Test
    fun testEnterAfterEndregionDoesNotContinueComment() = doEnterTest()

    @Test
    fun testEnterAfterBareRegionDoesNotContinueComment() = doEnterTest()

    @Ignore("[comment-continuation]")
    @Test
    fun testEnterInsideRegionBodyContinuesRegularComments() = doEnterTest()

    // --- Section N: signal declaration Enter ---

    @Test
    fun testEnterAfterBareSignalProducesPlainNewline() = doEnterTest()

    @Test
    fun testEnterInSignalParameterListSplitsArgs() = doEnterTest()

    // --- Section O: enum body Enter ---

    @Test
    fun testEnterAfterEnumOpenBraceIndentsBody() = doEnterTest()

    @Test
    fun testEnterAfterEnumMemberCommaPreservesIndent() = doEnterTest()

    @Ignore("[trailing-comma]")
    @Test
    fun testEnterAfterLastEnumMemberInsertsTrailingComma() = doEnterTest()

    @Test
    fun testEnterInsideAnonymousEnumSplitsToOnePerLine() = doEnterTest()

    // --- Section Q: Inner class Foo: block Enter ---

    @Test
    fun testEnterAfterInnerClassColonIndentsBody() = doEnterTest()

    @Test
    fun testEnterAfterInnerClassExtendsColonIndentsBody() = doEnterTest()

    @Test
    fun testEnterAfterAbstractInnerClassColonIndentsBody() = doEnterTest()

    // --- Section R: Named & typed-return lambda Enter ---

    @Test
    fun testEnterAfterNamedLambdaColonIndentsBody() = doEnterTest()

    @Test
    fun testEnterAfterTypedReturnLambdaColonIndentsBody() = doEnterTest()

    // --- Section S: Lua-style dict { key = v } Enter ---

    @Test
    fun testEnterAfterLuaDictKeyEquals() = doEnterTest()

    @Ignore("Requires [parser] changes")
    @Test
    fun testEnterInIncompleteLuaStyleDictKey() {
        doTypingTest("\n")
    }

    @Test
    fun testEnterAfterLuaDictMemberCommaPreservesOneTabIndent() = doEnterTest()

    // --- Section T: is not / not in operator-pair Enter ---

    @Ignore("[backslash]")
    @Test
    fun testEnterAfterIsNotKeywordSplitsWithBackslash() = doEnterTest()

    @Ignore("[backslash]")
    @Test
    fun testEnterAfterNotInKeywordSplitsWithBackslash() = doEnterTest()
}
