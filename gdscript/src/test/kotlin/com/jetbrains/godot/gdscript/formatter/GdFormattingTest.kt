package com.jetbrains.godot.gdscript.formatter

import com.intellij.application.options.CodeStyle
import com.intellij.formatting.WrapType
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.LocalTimeCounter
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdStmt
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

// Setup inspired by PythonCommonFormatterTest.
// The formatter is registered here programmatically because it is disabled in plugin.xml.
// TODO: FormatterTestCase base class does not add anything so interesting0
@RunWith(JUnit4::class)
class GdFormattingTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript").pathString

    private fun doTest() {
        val testPath = "formatter/" + getTestName(true)
        myFixture.configureByFile("$testPath.gd")
        WriteCommandAction.runWriteCommandAction(null) {
            val codeStyleManager = CodeStyleManager.getInstance(myFixture.project)
            val file = myFixture.file!!
            codeStyleManager.reformatText(file, 0, file.fileDocument.textLength)
        }
        myFixture.checkResultByFile("${testPath}_after.gd")
    }


    fun getCodeStyleSettings(): CodeStyleSettings = CodeStyle.getSettings(myFixture.project)

    fun getCommonCodeStyleSettings(): CommonCodeStyleSettings = getCodeStyleSettings().getCommonSettings(GdLanguage)

    fun getGdCodeStyleSettings(): GdCodeStyleSettings = getCodeStyleSettings().getCustomSettings(GdCodeStyleSettings::class.java)


    // Alignment

    @Ignore("[alignment] [space-only]")
    @Test
    fun testNoAlignForMethodArguments() {  // PY-3995
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS_IN_CALLS = false
        doTest()
    }

    @Ignore("[space-only]")
    @Test
    fun testAlignForMethodArguments() {  // PY-3995
        doTest()
    }

    @Ignore("TODO")
    @Test
    fun testAlignInBinaryExpression() = doTest()

    @Ignore("TODO")
    @Test
    fun testAlignInBinaryExpressions() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignInCallExpression() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignInParameterList() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignInParenthesizedExpression() = doTest()

    @Ignore("TODO: requires DICT_ALIGNMENT custom setting [space-only]")
    @Test
    fun testAlignDictLiteralOnValue() {
        // getGdCodeStyleSettings().DICT_ALIGNMENT = DICT_ALIGNMENT_ON_VALUE
        doTest()
    }

    @Ignore("TODO: requires DICT_ALIGNMENT custom setting [space-only]")
    @Test
    fun testAlignDictLiteralOnColon() {
        // getGdCodeStyleSettings().DICT_ALIGNMENT = DICT_ALIGNMENT_ON_COLON
        doTest()
    }

    @Ignore("TODO: requires DICT_ALIGNMENT custom setting [space-only]")
    @Test
    fun testAlignDictLiteralOnValueSubscriptionsAndSlices() {
        // getGdCodeStyleSettings().DICT_ALIGNMENT = DICT_ALIGNMENT_ON_VALUE
        doTest()
    }

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignmentInArgumentListWhereFirstArgumentIsEmptyCall() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignmentInListLiteralWhereFirstItemIsEmptyTuple() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignmentOfClosingBraceInDictLiteralWhenNoHangingIndent() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignmentOfClosingParenthesisOfArgumentListWhenNoHangingIndent() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignmentOfClosingParenthesisInNestedFunctionCallsWithSingleArgument() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignmentOfEmptyCollectionLiterals() = doTest()

    // --- Blank Lines ---

    @Test
    fun testBlankLineBetweenMethods() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS = 1
        doTest()
    }

    @Test
    fun testInnerClassMethodsKeepOneBlankLine() = doTest()

    @Test
    fun testBlankLineAroundClasses() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS = 1
        doTest()
    }

    @Test
    fun testBlankLineBeforeFunction() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS = 1
        doTest()
    }

    @Test
    fun testExtraBlankLinesBetweenMethodsAndAtTheEnd() {
        getCommonCodeStyleSettings().KEEP_BLANK_LINES_IN_DECLARATIONS = 1
        doTest()
    }

    @Test
    fun testBlankLineAfterDecorator() = doTest()

    @Test
    fun testBlankLinesAroundFirstMethodInsideClass() {
        getGdCodeStyleSettings().MAX_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS = 1
        getGdCodeStyleSettings().MIN_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS = 1
        doTest()
    }

    @Test
    fun testTrailingBlankLinesInEmptyFile() = doTest()

    @Ignore("Blank lines and \\ [postprocessor]")
    @Test
    fun testTrailingBlankLinesWithBackslashesAtFileEnd() = doTest()

    @Ignore("Blank lines and \\ [postprocessor]")
    @Test
    fun testTrailingBlankLinesWithBackslashesAtFunctionEnd() = doTest()

    @Ignore("Blank lines and \\ [postprocessor]")
    @Test
    fun testTrailingBlankLinesWithBackslashesMixed() = doTest()

    // --- Indentation preservation (idempotent: output = input) ---

    @Test
    fun testAnnotationParams() = doTest()

    @Test
    fun testIndentSimpleFunc() = doTest()

    @Test
    fun testIndentNestedBlocks() = doTest()

    @Test
    fun testIndentLambda() = doTest()

    @Test
    fun testIndentInArraySubscription() = doTest()

    // --- Indentation & Continuation ---

    @Test
    fun testContinuationIndent() = doTest()

    @Test
    fun testContinuationIndentBeforeFunctionArguments() {
        getGdCodeStyleSettings().USE_CONTINUATION_INDENT_FOR_ARGUMENTS = true
        doTest()
    }

    @Test
    fun testContinuationIndentBeforeFunctionParameters() = doTest()

    @Test
    fun testContinuationIndentForCallInStatementPart() {
        getGdCodeStyleSettings().USE_CONTINUATION_INDENT_FOR_ARGUMENTS = true
        doTest()
    }

    @Test
    fun testContinuationIndentInIndentingStatement() = doTest()

    @Test
    fun testContinuationIndentInIndentingStatement2() {
        getGdCodeStyleSettings().USE_CONTINUATION_INDENT_FOR_COLLECTIONS = true
        doTest()
    }

    @Test
    fun testNoContinuationIndentBeforeFunctionParameters() {
        getGdCodeStyleSettings().USE_CONTINUATION_INDENT_FOR_PARAMETERS = false
        doTest()
    }

    @Test
    fun testIndentAfterBackslash() = doTest()

    @Test
    fun testIndentAfterBackslashInBinaryExpressionInsideArray() = doTest()

    @Test
    fun testIndentAfterBackslashInBinaryExpressionAtStatementLevel() = doTest()

    @Test
    fun indentOfDotChainWithParentheses() = doTest()

    @Test
    fun testIndentOfDotChainWithBackslashes() = doTest()


    @Test
    fun testWrapInArraySubscription() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 120)
        doTest()
    }

    @Test
    fun testIndentOfCaseClausesInsideMatchStatement() = doTest()

    @Ignore("[parsing]")
    @Test
    fun testIndentOfCommentsInsideMatchStatement() = doTest()

    @Test
    fun testIndentOfTypeArguments() = doTest()

    @Test
    fun testIndentTypeArgumentsInFunctionTypeHint() = doTest()

    @Test
    fun testIndentOfCommentsInsideCallExpression() = doTest()

    @Test
    fun testIndentsWithTabsInsideDictLiteral() = doTest()

    @Test
    fun testIndentOfWrappedDictValue() = doTest()

    @Test
    fun testIndentOfLuaStyleDict() = doTest()

    @Test
    fun testIndentOfLuaStyleDictWrapped() = doTest()

    @Test
    fun testHangingIndentDetectionIgnoresComments() = doTest()

    @Test
    fun testHangingIndentInKeyValuePair() = doTest()

    @Test
    fun testHangingIndentInArgumentValue() = doTest()

    @Test
    fun testHangingIndentInParameterDefaultValue() {
        getGdCodeStyleSettings().SPACE_AROUND_EQ_IN_NAMED_PARAMETER = true
        doTest()
    }

    // --- Wrapping: Call Arguments & Parameters ---

    @Test
    fun testDefaultWrappingForCallArguments() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        doTest()
    }

    @Test
    fun testDefaultWrappingWithNewLineParensForCallArguments() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS_IN_CALLS = false
        getCommonCodeStyleSettings().CALL_PARAMETERS_LPAREN_ON_NEXT_LINE = true
        getCommonCodeStyleSettings().CALL_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        doTest()
    }

    @Test
    fun testForceNewLineAfterLeftParenInCallArguments() {
        getCommonCodeStyleSettings().CALL_PARAMETERS_LPAREN_ON_NEXT_LINE = true
        doTest()
    }

    @Ignore("TODO [space-only]")
    @Test
    fun testForceNewLineBeforeRightParenInCallArguments() {
        getCommonCodeStyleSettings().CALL_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        doTest()
    }


    @Test
    fun testForceNewLineBeforeRightParenNoAlignInCallArguments() {
        getCommonCodeStyleSettings().CALL_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS_IN_CALLS = false
        doTest()
    }

    @Test
    fun testWrappingChopDownIfLongForCallArguments() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getCommonCodeStyleSettings().CALL_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        doTest()
    }

    @Test
    fun testWrappingChopDownIfLongWithNewLineParensForCallArguments() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getCommonCodeStyleSettings().CALL_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS_IN_CALLS = false
        getCommonCodeStyleSettings().CALL_PARAMETERS_LPAREN_ON_NEXT_LINE = true
        getCommonCodeStyleSettings().CALL_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        doTest()
    }


    @Test
    fun testOptionalAlignForMethodParameters() {
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS = false
        doTest()
    }

    @Test
    fun testForceNewLineAfterLeftParenInMethodParameters() {
        getCommonCodeStyleSettings().METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE = true
        doTest()
    }

    @Ignore("TODO [space-only]")
    @Test
    fun testForceNewLineBeforeRightParenInMethodParameters() {
        getCommonCodeStyleSettings().METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        doTest()
    }


    @Test
    fun testForceNewLineBeforeRightParenNoAlignInMethodParameters() {
        getCommonCodeStyleSettings().METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS = false
        doTest()
    }

    @Test
    fun testDefaultWrappingForMethodParameters() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getGdCodeStyleSettings().SPACE_AROUND_EQ_IN_NAMED_PARAMETER = true
        doTest()
    }

    @Test
    fun testDefaultWrappingWithNewLineParensForMethodParameters() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS = false
        getCommonCodeStyleSettings().METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE = true
        getCommonCodeStyleSettings().METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        getGdCodeStyleSettings().SPACE_AROUND_EQ_IN_NAMED_PARAMETER = true
        doTest()
    }

    @Test
    fun testWrappingChopDownIfLongForMethodParameters() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getCommonCodeStyleSettings().METHOD_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        getGdCodeStyleSettings().SPACE_AROUND_EQ_IN_NAMED_PARAMETER = true
        doTest()
    }

    @Test
    fun testWrappingChopDownIfLongWithNewLineParensForMethodParameters() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getCommonCodeStyleSettings().METHOD_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS = false
        getCommonCodeStyleSettings().METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE = true
        getCommonCodeStyleSettings().METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE = true
        getGdCodeStyleSettings().SPACE_AROUND_EQ_IN_NAMED_PARAMETER = true
        doTest()
    }

    @Test
    fun testNoWrapBeforeParen() = doTest()

    // --- Wrapping: Collections ---

    @Test
    fun testListWrappingBracketsOnSameLine() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 40)
        getGdCodeStyleSettings().ARRAY_WRAPPING = CommonCodeStyleSettings.WRAP_AS_NEEDED
        doTest()
    }

    @Test
    fun testListWrappingFirstBracketOnNewLine() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 40)
        getGdCodeStyleSettings().ARRAY_WRAPPING = CommonCodeStyleSettings.WRAP_AS_NEEDED
        getGdCodeStyleSettings().ARRAY_NEW_LINE_AFTER_LEFT_BRACKET = true
        doTest()
    }

    @Ignore("TODO: requires LIST_WRAPPING + LIST_NEW_LINE_BEFORE_RIGHT_BRACKET custom settings [space-only]")
    @Test
    fun testListWrappingLastBracketOnNewLine() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 40)
        // getGdCodeStyleSettings().LIST_WRAPPING = CommonCodeStyleSettings.WRAP_AS_NEEDED
        // getGdCodeStyleSettings().LIST_NEW_LINE_BEFORE_RIGHT_BRACKET = true
        doTest()
    }

    @Ignore("TODO: requires LIST_WRAPPING + LIST_NEW_LINE_BEFORE_RIGHT_BRACKET + ALIGN_COLLECTIONS_AND_COMPREHENSIONS custom settings")
    @Test
    fun testListWrappingDoNotAlign() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 40)
        // getGdCodeStyleSettings().LIST_WRAPPING = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        // getGdCodeStyleSettings().LIST_NEW_LINE_BEFORE_RIGHT_BRACKET = true
        // getGdCodeStyleSettings().ALIGN_COLLECTIONS_AND_COMPREHENSIONS = false
        doTest()
    }

    @Test
    fun testListWrappingBracketsOnNewLine() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 40)
        getGdCodeStyleSettings().ARRAY_WRAPPING = CommonCodeStyleSettings.WRAP_AS_NEEDED
        getGdCodeStyleSettings().ARRAY_NEW_LINE_AFTER_LEFT_BRACKET = true
        getGdCodeStyleSettings().ARRAY_NEW_LINE_BEFORE_RIGHT_BRACKET = true
        doTest()
    }

    @Test
    fun testListChopDownBracketsOnSameLine() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 40)
        getGdCodeStyleSettings().ARRAY_WRAPPING = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        doTest()
    }

    @Test
    fun testListChopDownBracketsOnNewLine() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 40)
        getGdCodeStyleSettings().ARRAY_WRAPPING = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        getGdCodeStyleSettings().ARRAY_NEW_LINE_AFTER_LEFT_BRACKET = true
        getGdCodeStyleSettings().ARRAY_NEW_LINE_BEFORE_RIGHT_BRACKET = true
        doTest()
    }

    @Test
    fun testDictWrappingChopDownIfLong() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        getGdCodeStyleSettings().DICT_WRAPPING = WrapType.CHOP_DOWN_IF_LONG.legacyRepresentation
        doTest()
    }

    @Test
    fun testForceNewLineAfterLeftBraceInDict() {
        getGdCodeStyleSettings().DICT_NEW_LINE_AFTER_LEFT_BRACE = true
        doTest()
    }

    @Ignore("TODO: requires DICT_NEW_LINE_BEFORE_RIGHT_BRACE custom setting [space-only]")
    @Test
    fun testForceNewLineBeforeRightBraceInDict() {
        // getGdCodeStyleSettings().DICT_NEW_LINE_BEFORE_RIGHT_BRACE = true
        doTest()
    }

    @Test
    fun testForceNewLineBeforeRightBraceInDictAfterColon() {
        getGdCodeStyleSettings().DICT_NEW_LINE_BEFORE_RIGHT_BRACE = true
        doTest()
    }

    @Test
    fun testHangClosingBracketsInCollectionLiterals() {
        getGdCodeStyleSettings().HANG_CLOSING_BRACKETS = true
        doTest()
    }

    @Test
    fun testHangClosingParenthesisInFunctionCall() {
        getGdCodeStyleSettings().HANG_CLOSING_BRACKETS = true
        doTest()
    }

    @Test
    fun testHangClosingParenthesisInFunctionDefinition() {
        getGdCodeStyleSettings().HANG_CLOSING_BRACKETS = true
        doTest()
    }

    @Test
    fun testDictLiteral() = doTest()

    // --- Comments ---

    @Test
    fun testComment() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testCommentBetweenClasses() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testInlineCommentInsideBracket() = doTest()

    @Test
    fun testCommentInEmptyArray() = doTest()

    @Test
    fun testCommentAfterBlock() = doTest()

    @Test
    fun testCommentedCodeFragmentIgnored() = doTest()

    @Test
    fun testCommentsSpacing() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Ignore("[parsing]")
    @Test
    fun testTrailingBlockCommentsIndentationPreserved() {
        doTest()
    }

    @Ignore("Comment [postprocessor]")
    @Test
    fun testTrailingComment() = doTest()

    @Ignore("Comment [postprocessor]")
    @Test
    fun testWhitespaceInsertedAfterHashSignInMultilineComment() = doTest()

    @Test
    fun testDoubleHashCommentIgnored() = doTest()

    // --- Literals ---


    @Test
    fun testNestedClosingBracketAlignment() = doTest()

    // --- Conditions & Control Flow ---

    @Test
    fun testIfConditionContinuation() = doTest()

    @Test
    fun testMultilineIfConditionComplex() = doTest()

    @Test
    fun testMultilineIfConditionKeywordAtStart() = doTest()

    @Test
    fun testMultilineIfConditionKeywordAtEnd() = doTest()

    @Test
    fun testMultilineIfConditionLessComparisonsKeywordAtEnd() = doTest()

    @Test
    fun testMultilineIfConditionNestedExpressions() = doTest()

    @Test
    fun testMultilineIfConditionInParenthesesKeywordAtStart() = doTest()

    @Test
    fun testMultilineIfConditionInParenthesesKeywordAtEnd() = doTest()

    @Test
    fun testMultilineIfConditionInParenthesesKeywordAtEndSecondOperandIsReference() = doTest()

    @Test
    fun testMultilineIfConditionInParenthesesNegatedKeywordAtEnd() = doTest()

    @Test
    fun testMultilineIfConditionInParenthesesNestedExpressions() = doTest()

    @Test
    fun testMultilineIfConditionInParenthesesHangingIndent() = doTest()

    @Test
    fun testMultilineElifCondition() = doTest()

    @Test
    fun testMultilineElifConditionInParentheses() = doTest()

    @Ignore("[wrap] [backslash-insertion]")
    @Test
    fun testWrapBeforeElse() = doTest()


    @Test
    fun testNewLineAfterColon() {
        getGdCodeStyleSettings().NEW_LINE_AFTER_COLON = true
        doTest()
    }

    @Test
    fun testNewLineAfterColonPreservesNonBodyColons() {
        getGdCodeStyleSettings().NEW_LINE_AFTER_COLON = true
        doTest()
    }

    @Test
    fun testNewLineAfterColonNoOpWhenMultiline() {
        getGdCodeStyleSettings().NEW_LINE_AFTER_COLON = true
        doTest()
    }

    @Test
    fun testNewLineAfterColonDisabledKeepsSingleLine() = doTest()

    // --- Pattern Matching / Match ---

    @Test
    fun testSpacesAfterMatchKeyword() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testItemAlignmentInArrayPatterns() = doTest()

    @Test
    fun testIndentOfArrayPatternInMatchStatement() = doTest()

    @Test
    fun testIndentOfArrayPatternInMatchStatementHanging() {
        getGdCodeStyleSettings().HANG_CLOSING_BRACKETS = true
        doTest()
    }

    @Ignore("TODO [space-only]")
    @Test
    fun testItemAlignmentInNestedArrayPatterns() = doTest()

    @Ignore("TODO [space-only]")
    @Test
    fun testAlignmentOfDictPatternInMatchStatement() = doTest()

    @Test
    fun testIndentOfDictPatternInMatchStatement() = doTest()

    @Test
    fun testIndentOfDictPatternInMatchStatementHanging() {
        getGdCodeStyleSettings().HANG_CLOSING_BRACKETS = true
        doTest()
    }

    @Ignore("[alingment]")
    @Test
    fun testAlternativesAlignmentInOrPatterns() = doTest()

    @Test
    fun testSpacesWithinBracketsInArrayPatterns() {
        getCommonCodeStyleSettings().SPACE_WITHIN_BRACKETS = true
        doTest()
    }


    @Test
    fun testSpacesWithinBracesInDictPatterns() {
        getCommonCodeStyleSettings().SPACE_WITHIN_BRACES = true
        doTest()
    }

    @Test
    fun testSpacesBeforeAndAfterCommasInPatterns() {
        getCommonCodeStyleSettings().SPACE_BEFORE_COMMA = true
        getCommonCodeStyleSettings().SPACE_AFTER_COMMA = false
        doTest()
    }

    @Test
    fun testSpacesBeforeAndAfterColonsInPatterns() {
        getGdCodeStyleSettings().SPACE_BEFORE_COLON = true
        getGdCodeStyleSettings().SPACE_AFTER_COLON = false
        doTest()
    }

    @Test
    fun testNormalIndentInsideCollectionLiterals() {
        doTest()
    }

    @Test
    fun testContinuationIndentInsideCollectionLiterals() {
        getGdCodeStyleSettings().USE_CONTINUATION_INDENT_FOR_COLLECTIONS = true
        doTest()
    }

    @Test
    fun testContinuationIndentDoesNotApplyToClosingBracketsWhenNotHanging() {
        getGdCodeStyleSettings().USE_CONTINUATION_INDENT_FOR_COLLECTIONS = true
        doTest()
    }

    // --- Trailing Commas ---
    @Ignore("[trailing-comma]")
    @Test
    fun testTrailingCommaInArgumentList() {
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_ARGUMENTS_LIST = true
        doTest()
    }

    @Ignore("[trailing-comma]")
    @Test
    fun testTrailingCommaInParameterList() {
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_PARAMETER_LIST = true
        doTest()
    }

    @Ignore("[trailing-comma]")
    @Test
    fun testTrailingCommaInListLiteral() {
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_COLLECTIONS = true
        doTest()
    }

    @Ignore("[trailing-comma]")
    @Test
    fun testTrailingCommaInDictLiteral() {
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_COLLECTIONS = true
        doTest()
    }

    @Ignore("[trailing-comma]")
    @Test
    fun testTrailingCommaMixed() {
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_COLLECTIONS = true
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_PARAMETER_LIST = true
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_ARGUMENTS_LIST = true
        doTest()
    }

    // --- Misc (Category 13) ---

    @Test
    fun testWrapAssignment() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 120)
        doTest()
    }

    @Ignore("Adding \\ processing [backslash]")
    @Test
    fun testWrapInBinaryExpression() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        doTest()
    }

    @Ignore("[alignment] [space-only] [wrap]")
    @Test
    fun testWrapOnDotAlignment() = doTest()

    @Test
    fun testWrapOnDot() = doTest()

    @Test
    fun testWrapDefinitionWithLongLine() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 30)
        doTest()
    }

    @Test
    fun testBeforeTopLevelClass() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testTwoLinesBetweenTopLevelClasses() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testTwoLinesBetweenTopLevelFunctions() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testTwoLinesBetweenTopLevelDeclarationsWithComment() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testTwoLinesBetweenTopLevelStatementAndDeclarationsWithComment() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testVariableAnnotations() = doTest()

    @Ignore("[alignment] [space-only]")
    @Test
    fun testChainedMethodCallsInParenthesesAlignment() = doTest()

    @Test
    fun testChainedMethodCallsInParentheses() = doTest()

    @Ignore("[alignment] [space-only]")
    @Test
    fun testChainedAttributeAccessInParenthesesAlignment() = doTest()

    @Test
    fun testChainedAttributeAccessInParentheses() = doTest()

    @Test
    fun testReformatOfSingleElementPossible() {
        myFixture.configureByFile("formatter/${getTestName(true)}.gd")
        WriteCommandAction.runWriteCommandAction(myFixture.project) {
            val elementAtCaret = PsiUtilCore.getElementAtOffset(myFixture.file, myFixture.caretOffset)
            val statement = PsiTreeUtil.getParentOfType(elementAtCaret, GdStmt::class.java, false)
            assertNotNull(statement)
            val codeStyleManager = CodeStyleManager.getInstance(myFixture.project)
            codeStyleManager.reformat(statement!!)
        }
        myFixture.checkResultByFile("formatter/${getTestName(true)}_after.gd")
    }

    @Test
fun testHangingIndentsInMultilineCallChainInParenthesis() = doTest()

    @Ignore("[alignment] [space-only]")
    @Test
    fun testHangingIndentsInMultilineCallChainInSquareBracketsAlignment() = doTest()

    @Test
    fun testHangingIndentsInMultilineCallChainInSquareBrackets() = doTest()

    @Test
    fun testNotParenthesisedBinaryExpressions() = doTest()

    @Test
    fun testMultiLineCallChainSplitByBackslashes() = doTest()

    @Test
    fun testWrappingInCollectionsCommentsStayOnTheSameLine() {
        // getGdCodeStyleSettings().DICT_WRAPPING = CommonCodeStyleSettings.WRAP_ALWAYS
        // getGdCodeStyleSettings().LIST_WRAPPING = CommonCodeStyleSettings.WRAP_ALWAYS
        getCommonCodeStyleSettings().METHOD_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_ALWAYS
        getCommonCodeStyleSettings().CALL_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_ALWAYS
        doTest()
    }


    @Test
    fun testNoAlignmentForMultilineBinaryExpressionInReturnStatement() = doTest()

    @Test
    fun testNoAlignmentForSplitByBackslashesTupleInAssignmentStatement() = doTest()

    @Ignore("TODO: requires DICT_NEW_LINE_AFTER_LEFT_BRACE custom setting")
    @Test
    fun testNoAlignmentClosingBraceInDictLiteralWhenOpeningBraceIsForcedOnNewLine() {
        // getGdCodeStyleSettings().DICT_NEW_LINE_AFTER_LEFT_BRACE = true
        doTest()
    }

    @Ignore("TODO [alignment] [space-only]")
    @Test
    fun testNoAlignmentAfterDictHangingIndentInFunctionCall() {
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS_IN_CALLS = true
        doTest()
    }

    @Ignore("TODO [alignment] [editing]?")
    @Test
    fun testNoAlignmentAfterDictHangingIndentInFunctionCallOnTyping() {
        getCommonCodeStyleSettings().ALIGN_MULTILINE_PARAMETERS_IN_CALLS = true
        val testName = "formatter/${getTestName(true)}"
        myFixture.configureByFile("$testName.gd")
        myFixture.type("\n[")
        myFixture.checkResultByFile("${testName}_after.gd")
    }

    @Test
    fun testSpaceAfterTrailingCommaIfNoSpaceAfterCommaButWithinBracesOrBrackets() {
        getCommonCodeStyleSettings().SPACE_WITHIN_BRACES = true
        getCommonCodeStyleSettings().SPACE_WITHIN_BRACKETS = true
        getCommonCodeStyleSettings().SPACE_AFTER_COMMA = false
        doTest()
    }

    @Test
    fun testSpaceAfterTrailingCommaInDictLiterals() = doTest()

    @Test
    fun testSpacesInsideParenthesisAreStripped() = doTest()

    // --- Spacing ---

    @Test
    fun testSpacingComma() = doTest()

    @Test
    fun testSpacingExtraneousWhitespace() = doTest()

    @Test
    fun testSpacingAroundDefaultParameterValue() {
        getGdCodeStyleSettings().SPACE_AROUND_EQ_IN_NAMED_PARAMETER = true
        doTest()
    }

    @Test
    fun testSpacingAroundOperators() = doTest()

    @Test
    fun testSplitSingleLineCompoundStatements() {
        getGdCodeStyleSettings().NEW_LINE_AFTER_COLON = true
        doTest()
    }

    @Test
    fun testUnaryMinus() = doTest()

    @Test
    fun testSpaceInMethodDeclaration() {
        getCommonCodeStyleSettings().SPACE_BEFORE_METHOD_PARENTHESES = true
        doTest()
    }

    @Test
    fun testSpaceInLambdaDeclaration() {
        getGdCodeStyleSettings().SPACE_BEFORE_LAMBDA_PARENTHESES = true
        doTest()
    }

    @Test
    fun testSpaceInsideLuaStyleDict() {
        doTest()
    }

    @Test
    fun testSpaceAroundKeywords() = doTest()

    @Test
    fun testSpaceInAnnotations() = doTest()

    @Test
    fun testSpaceWithinBraces() {
        getCommonCodeStyleSettings().SPACE_WITHIN_BRACES = true
        doTest()
    }

    @Test
    fun testSpaceWithinDeclarationParentheses() {
        getCommonCodeStyleSettings().SPACE_WITHIN_METHOD_PARENTHESES = true
        getCommonCodeStyleSettings().SPACE_WITHIN_EMPTY_METHOD_PARENTHESES = false
        doTest()
    }

    @Test
    fun testSpaceWithinCallParentheses() {
        getCommonCodeStyleSettings().SPACE_WITHIN_METHOD_CALL_PARENTHESES = true
        getCommonCodeStyleSettings().SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES = false
        doTest()
    }

    @Test
    fun testSpaceBetweenParenthesesInEmptyArgumentList() {
        getCommonCodeStyleSettings().SPACE_WITHIN_METHOD_CALL_PARENTHESES = false
        getCommonCodeStyleSettings().SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES = true
        doTest()
    }

    @Test
    fun testSpaceBetweenParenthesesInEmptyParameterList() {
        getCommonCodeStyleSettings().SPACE_WITHIN_METHOD_PARENTHESES = false
        getCommonCodeStyleSettings().SPACE_WITHIN_EMPTY_METHOD_PARENTHESES = true
        doTest()
    }

    @Test
    fun testSpaceAroundDot() = doTest()

    @Test
    fun testForceSpacesAroundEqualSignInAnnotatedParameter() {
        getGdCodeStyleSettings().SPACE_AROUND_EQ_IN_NAMED_PARAMETER = true
        doTest()
    }

    @Test
    fun testLambdaColon() = doTest()

    @Test
    fun testSpacingOperators() = doTest()

    // --- Signals ---

    @Test
    fun testSignalDeclarationNoParams() = doTest()

    @Test
    fun testSignalDeclarationWithParams() = doTest()

    @Test
    fun testSignalDeclarationWithTypedParams() = doTest()

    @Test
    fun testSignalsRespectKeepBlankLinesInDeclarations() = doTest()

    @Test
    fun testRemoveRedundantBlankLinesInsideIf() = doTest()

    @Test
    fun testBlankLineBetweenSignalsAndMethods() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testBlankLineBetweenSignalsAndVars() = doTest()

    @Test
    fun testSignalAfterClassHeader() = doTest()

    @Test
    fun testSignalEmptyParensVsBareSignalPreserved() = doTest()

    @Test
    fun testSignalDefaultWrappingForParams() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 80)
        doTest()
    }

    @Ignore("[paren-on-new-line]")
    @Test
    fun testSignalForceNewLineAfterLeftParen() {
        getCodeStyleSettings().METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE = true
        doTest()
    }

    @Test
    fun testSignalWrappingChopDownIfLong() {
        getCodeStyleSettings().setRightMargin(GdLanguage, 60)
        getCommonCodeStyleSettings().METHOD_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_ON_EVERY_ITEM
        doTest()
    }

    @Ignore("[trailing-comma]")
    @Test
    fun testSignalTrailingCommaInParams() {
        // getGdCodeStyleSettings().USE_TRAILING_COMMA_IN_SIGNAL_PARAMETER_LIST = true
        doTest()
    }

    // --- Headers: class_name / extends / @abstract ---

    @Test
    fun testClassNameOnly() = doTest()

    @Test
    fun testExtendsOnly() = doTest()

    @Test
    fun testClassNameThenExtends() = doTest() // Probably fails, EndStmt keeps its own blank line

    @Test
    fun testClassNameExtendsCombined() = doTest()

    @Test
    fun testClassHeaderStyle_ForceCombined() {
        getGdCodeStyleSettings().CLASS_HEADER_STYLE = GdCodeStyleSettings.CLASS_HEADER_STYLE_FORCE_COMBINED
        doTest()
    }

    @Test
    fun testClassHeaderStyle_ForceSeparate() {
        getGdCodeStyleSettings().CLASS_HEADER_STYLE = GdCodeStyleSettings.CLASS_HEADER_STYLE_FORCE_SEPARATE
        doTest()
    }

    @Test
    fun testExtendsStringPath() = doTest()

    @Test
    fun testInnerClassWithExtends() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testBlankLineAfterHeader() = doTest()

    @Test
    fun testAbstractAnnotationBeforeClassName() = doTest()

    @Test
    fun testTwoBlankLinesAfterHeaderBeforeFunc() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    // --- Style Guide ---

    @Test
    fun testStaticTypingColonSpacing() = doTest()

    @Test
    fun testReturnTypeArrowSpacing() = doTest()


    @Test
    fun testStyleGuideCanonicalFileStructure() = doTest()

    @Test
    fun testTwoBlankLinesBeforeStaticFunc() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testBlankLinesAroundInnerClass() {
        getGdCodeStyleSettings().MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS = 2
        doTest()
    }

    @Test
    fun testNoTrailingWhitespaceInBlankLinesInsideFunc() = doTest()

    @Test
    fun testDeclarationSpacingNormalization() = doTest()

    @Test
    fun testTwoTabContinuationIndentForWrappedStatement() {
        getGdCodeStyleSettings().USE_CONTINUATION_INDENT_FOR_ARGUMENTS = true
        doTest()
    }

    @Ignore("Nice to have and [alignment]")
    @Test
    fun testLuaStyleDictSingleLineHasSpacesInsideBraces() = doTest()

    @Test
    fun testLeadingWhitespaceNormalizedToTabs() {
        getCommonCodeStyleSettings().indentOptions!!.USE_TAB_CHARACTER = true
        doTest()
    }


    @Test
    fun testLeadingWhitespaceNormalizedToSpaces() {
        getCommonCodeStyleSettings().indentOptions!!.USE_TAB_CHARACTER = false
        getCommonCodeStyleSettings().indentOptions!!.INDENT_SIZE = 4
        doTest()
    }

    // Reproduces the code-style preview corruption: the INDENT_SETTINGS sample
    // (3-level nesting -> multi-level dedent, plus multi-lino e collection literals),
    // written with tabs, re-indented to spaces.
    @Test
    fun testIndentSampleTabsToSpaces() {
        getCommonCodeStyleSettings().indentOptions!!.USE_TAB_CHARACTER = false
        getCommonCodeStyleSettings().indentOptions!!.INDENT_SIZE = 4
        doTest()
    }

    // Regression for the code-style preview: reformatting the INDENT sample on a NON-PHYSICAL file
    // (eventSystemEnabled = false, as CodeStyleAbstractPanel/PsiFileFactory create it) must rewrite
    // GDScript's NEW_LINE/INDENT leading whitespace correctly. Without GdFormattingModel this path
    // (PsiBasedFormattingModel) left stale tabs and injected blank lines. See [GdFormattingModel].
    @Test
    fun testIndentSamplePreviewNonPhysicalPath() = doPreviewPathTest(eventSystemEnabled = false)

    // Same content on an EVENT-SYSTEM-ENABLED in-memory file. The lever is not eventSystemEnabled
    // but the document-less (PSI-based) whitespace replacement, so this must pass too.
    @Test
    fun testIndentSamplePreviewEventEnabled() = doPreviewPathTest(eventSystemEnabled = true)

    private fun doPreviewPathTest(eventSystemEnabled: Boolean) {
        getCommonCodeStyleSettings().indentOptions!!.USE_TAB_CHARACTER = false
        getCommonCodeStyleSettings().indentOptions!!.INDENT_SIZE = 4

        val tab = "\t"
        val before = buildString {
            append("extends Node\n\n")
            append("func process(items):\n")
            append("${tab}for item in items:\n")
            append("$tab${tab}if item > 0:\n")
            append("$tab$tab${tab}print(item)\n")
            append("${tab}var matrix := [\n")
            append("$tab$tab[1, 2, 3],\n")
            append("$tab$tab[4, 5, 6],\n")
            append("$tab]\n")
            append("${tab}var config := {\n")
            append("$tab$tab\"width\": 800,\n")
            append("$tab$tab\"height\": 600,\n")
            append("$tab}\n")
        }
        val expected = buildString {
            append("extends Node\n\n")
            append("func process(items):\n")
            append("    for item in items:\n")
            append("        if item > 0:\n")
            append("            print(item)\n")
            append("    var matrix := [\n")
            append("        [1, 2, 3],\n")
            append("        [4, 5, 6],\n")
            append("    ]\n")
            append("    var config := {\n")
            append("        \"width\": 800,\n")
            append("        \"height\": 600,\n")
            append("    }\n")
        }

        val psiFile = PsiFileFactory.getInstance(myFixture.project)
            .createFileFromText("a.gd", GdFileType, before, LocalTimeCounter.currentTime(), eventSystemEnabled)
        WriteCommandAction.runWriteCommandAction(myFixture.project) {
            CodeStyleManager.getInstance(myFixture.project).reformat(psiFile)
        }
        assertEquals(expected, psiFile.text)
    }

    // --- Special Strings & Node-Path Shorthands ---

    @Test
    fun testRawDoubleQuotedStringPreserved() = doTest()

    @Test
    fun testRawSingleQuotedStringPreserved() = doTest()

    @Test
    fun testTripleDoubleQuotedStringPreserved() = doTest()

    @Test
    fun testTripleSingleQuotedStringPreserved() = doTest()

    @Test
    fun testStringNameLiteralPreserved() = doTest()

    @Test
    fun testNodePathLiteralPreserved() = doTest()

    @Test
    fun testStringContentsWithGdscriptTokensNotReformatted() = doTest()

    @Ignore("[parsing] of raw triple quoted strings")
    @Test
    fun testTripleQuotedRawDoubleStringPreserved() = doTest()

    @Ignore("[parsing] of raw triple quoted strings")
    @Test
    fun testTripleQuotedRawSingleStringPreserved() = doTest()

    @Test
    fun testDollarNodePathShorthandPreserved() = doTest()

    @Test
    fun testPercentUniqueNameShorthandPreserved() = doTest()

    // --- Property Setters / Getters ---

    @Test
    fun testPropertyInlineSetterShorthand() = doTest()

    @Test
    fun testPropertyFullSetBlock() = doTest()

    @Test
    fun testPropertyFullSetGetBlock() = doTest()

    @Test
    fun testPropertySpacing() = doTest()

    // --- GDScript Operators & Expressions ---

    @Test
    fun testSuperCallSpacing() = doTest()

    @Test
    fun testAwaitExpressionSpacing() = doTest()

    @Test
    fun testIsNotOperatorSpacing() = doTest()

    @Test
    fun testNotInOperatorSpacing() = doTest()

    @Test
    fun testMatchWhenGuardSpacing() = doTest()

    // --- Inline tests ---

    private fun doTextTest(before: String, after: String) {
        myFixture.configureByText("a.gd", before.trimIndent())
        WriteCommandAction.runWriteCommandAction(null) {
            CodeStyleManager.getInstance(myFixture.project).reformat(myFixture.file!!)
        }
        myFixture.checkResult(after.trimIndent())
    }

    @Test
    fun testLambdaInConnectIndent() {
        val code = """
            func stop_counting_on_signal(the_signal):
            ${"\t"}the_signal.connect(func():
            ${"\t\t"}pass)
        """
        doTextTest(code, code)
    }

    @Test
    fun testLambdaInConnectIndent2() {
        val code = """
            func stop_counting_on_signal(the_signal):
            ${"\t"}the_signal.connect(func():
            ${"\t\t"}the_signal.connect(func():
            ${"\t\t\t"}pass))
        """
        doTextTest(code, code)
    }
}