package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.GdCodeInsightTestBase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Assertions.assertTrue
import org.opentest4j.AssertionFailedError
import java.io.File
import kotlin.io.path.pathString

abstract class GdParsingTestCase : GdCodeInsightTestBase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases").pathString
    }

    /**
     * Parses the test data file and optionally compares the PSI tree against a golden .txt file.
     * Mirrors the behaviour of [com.intellij.testFramework.ParsingTestCase.doTest].
     */
    protected fun doTest(checkResult: Boolean, ensureNoErrorElements: Boolean = false) {
        val testName = getTestName(false)
        val testDataPath = myFixture.testDataPath
        val text = File("$testDataPath/$testName.gd").readText()

        // Create an in-memory PSI file (same as ParsingTestCase.createFile)
        val file = myFixture.configureByText("$testName.gd", text)

        if (ensureNoErrorElements) {
            val errors = PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)
            assertTrue(errors.isEmpty(), "Found unexpected parse errors: ${errors.map { it.errorDescription }}")
        }

        if (checkResult) {
            // Use the same format as ParsingTestCase.toParseTreeText(file, skipSpaces=false, printRanges=false)
            val actual = DebugUtil.psiToString(file, false, false)
            val expectedFile = File("$testDataPath/$testName.txt")

            if (!expectedFile.exists()) {
                expectedFile.writeText(actual)
                throw AssertionError(
                    "Golden file ${expectedFile.absolutePath} did not exist — created from actual output. Re-run the test."
                )
            }

            val expected = expectedFile.readText()
            if (expected != actual) {
                throw AssertionFailedError(
                    "Parser tree mismatch for $testName — expected file: ${expectedFile.absolutePath}",
                    expected, actual
                )
            }
        }
    }
}
